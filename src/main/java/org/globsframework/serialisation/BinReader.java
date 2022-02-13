package org.globsframework.serialisation;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.fields.FieldVisitor;
import org.globsframework.metamodel.fields.GlobField;
import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.model.Glob;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.model.FieldNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.globsframework.serialisation.WireConstants.Type.END_GLOB;

public class BinReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinReader.class);
    private CodedInputStream inputStream;
    private Map<String, GlobBinReader.FieldByNumberReader> readerMap = new ConcurrentHashMap<>();


    public static BinReader create() {
        return new BinReader();
    }

    public GlobBinReader create(InputStream inputStream) {
        return new GlobBinReader(inputStream, this);
    }

    static class GlobBinReader {
        private final CodedInputStream inputStream;
        private BinReader binReader;

        public GlobBinReader(InputStream inputStream, BinReader binReader) {
            this.inputStream = CodedInputStream.newInstance(inputStream);
            this.binReader = binReader;
        }

        public Glob read(GlobType globType) throws IOException {
            int tag = inputStream.readTag();
            if (WireConstants.getTagWireType(tag) != WireConstants.Type.START_GLOB) {
                throw new RuntimeException("Expecting Glob but got " + tag + " : " + WireConstants.getTagWireType(tag));
            }
            String typeName = inputStream.readUtf8String();
            if (!globType.getName().equals(typeName)) {
                throw new RuntimeException("Expecting " + globType.getName() + " but found " + typeName);
            }
            FieldByNumberReader fieldByNumberReader = binReader.readerMap.computeIfAbsent(typeName, s -> createReader(globType));
            MutableGlob data = globType.instantiate();
            while (true) {
                int fieldTag = inputStream.readTag();
                int fieldNumber = WireConstants.getTagFieldNumber(fieldTag);
                int tagWireType = WireConstants.getTagWireType(fieldTag);
                if (tagWireType == END_GLOB) {
                    return data;
                }
                fieldByNumberReader.get(fieldNumber)
                        .read(data, fieldTag, tagWireType, inputStream);
            }
        }

        private FieldByNumberReader createReader(GlobType globType) {
            Field[] fields = globType.getFields();
            List<Reader> readerList = new ArrayList<>();
            for (Field field : fields) {
                field.findOptAnnotation(FieldNumber.key)
                        .map(FieldNumber.fieldNumber)
                        .map(fNumber -> field.safeVisit(new FieldVisitor.AbstractFieldVisitor() {
                            public void visitInteger(IntegerField field) throws Exception {
                                readerList.add(new IntegerReader(fNumber, field));
                            }

                            public void visitGlob(GlobField field) throws Exception {
                                readerList.add(new GlobReader(fNumber, field));
                            }
                        }));
            }
            Integer size = readerList.stream().map(Reader::getFieldNumber).max(Integer::compareTo).orElse(0);
            Reader[] readers = new Reader[size + 1];
            Arrays.fill(readers, UnknownFieldReader.unknownFieldReader);
            for (Reader reader : readerList) {
                readers[reader.getFieldNumber()] = reader;
            }
            return new FieldByNumberReader(readers);
        }

        interface Reader {
            void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) throws IOException;

            int getFieldNumber();

        }

        static class FieldByNumberReader {
            private final Reader[] readers;

            public FieldByNumberReader(Reader[] readers) {
                this.readers = readers;
            }

            public Reader get(int fieldNumber) {
                return fieldNumber < readers.length ? readers[fieldNumber] : UnknownFieldReader.unknownFieldReader;
            }
        }

        static class UnknownFieldReader implements Reader {
            static final UnknownFieldReader unknownFieldReader = new UnknownFieldReader();

            public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) throws IOException {
                inputStream.skipField(tag);
            }

            public int getFieldNumber() {
                return 0;
            }

        }


        static class IntegerReader implements Reader {
            private final IntegerField field;
            private int fieldNumber;

            IntegerReader(int fieldNumber, IntegerField field) {
                this.fieldNumber = fieldNumber;
                this.field = field;
            }

            public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) throws IOException {
                switch (tagWireType) {
                    case WireConstants.Type.NULL:
                        data.set(field, null);
                        break;
                    case WireConstants.Type.INT:
                        data.set(field, inputStream.readInt());
                        break;
                    default:
                        throw new RuntimeException("For " + field.getName() + " unexpected type " + tagWireType);
                }
            }

            public int getFieldNumber() {
                return fieldNumber;
            }

        }

        private class GlobReader implements Reader {
            private Integer fNumber;
            private GlobField field;

            public GlobReader(Integer fNumber, GlobField field) {
                this.fNumber = fNumber;
                this.field = field;
            }

            public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) throws IOException {
                switch (tagWireType) {
                    case WireConstants.Type.NULL:
                        data.set(field, null);
                        break;
                    case WireConstants.Type.GLOB:
                        data.set(field, GlobBinReader.this.read(field.getTargetType()));
                        break;
                    default:
                        String message = "For " + field.getName() + " unexpected type " + tagWireType;
                        LOGGER.error(message);
                        inputStream.skipField(tag);
                }
            }

            public int getFieldNumber() {
                return fNumber;
            }
        }
    }
}
