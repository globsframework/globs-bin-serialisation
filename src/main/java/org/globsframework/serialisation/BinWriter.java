package org.globsframework.serialisation;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.fields.FieldVisitor;
import org.globsframework.metamodel.fields.GlobField;
import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.model.FieldNumber;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BinWriter {
    private final Map<String, GlobWriter.FieldWithNumber> writers = new ConcurrentHashMap<>();

    public static BinWriter create(){
        return new BinWriter();
    }

    GlobWriter create(OutputStream outputStream) {
        return new GlobWriter(outputStream, this);
    }

    public static class GlobWriter {
        private final OutputStream outputStream;
        private final CodedOutputStream codedOutputStream;
        private BinWriter binWriter;

        public GlobWriter(OutputStream outputStream, BinWriter binWriter) {
            this.outputStream = outputStream;
            codedOutputStream = CodedOutputStream.newInstance(outputStream);
            this.binWriter = binWriter;
        }

        public void write(Glob glob) throws IOException {
            String typeName = glob.getType().getName();
            FieldWithNumber fieldWithNumber = binWriter.writers.computeIfAbsent(typeName, s -> createWriter(glob.getType()));
            codedOutputStream.writeStartGlob(typeName);
            for (Write write : fieldWithNumber.writes) {
                write.write(codedOutputStream, glob);
            }
            codedOutputStream.writeEndGlob();
        }

        private FieldWithNumber createWriter(GlobType type) {
            Field[] fields = type.getFields();
            List<Write> writes = new ArrayList<>();
            for (Field field : fields) {
                field.findOptAnnotation(FieldNumber.key)
                        .map(FieldNumber.fieldNumber)
                        .map(index ->
                                field.safeVisit(new FieldVisitor.AbstractFieldVisitor() {
                                    public void visitInteger(IntegerField field) {
                                        writes.add(new IntWrite(index, field));
                                    }

                                    public void visitGlob(GlobField field) {
                                        writes.add(new GlobWrite(index, field));
                                    }
                                }));
            }
            int maxLen = writes.stream().map(Write::getFieldNumber).max(Integer::compareTo).orElse(0);
            Write[] realWrites = new Write[maxLen + 1];
            Arrays.fill(realWrites, NullWrite.nullWrite);
            for (Write write : writes) {
                realWrites[write.getFieldNumber()] = write;
            }
            return new FieldWithNumber(realWrites);
        }

        public interface Write {
            void write(CodedOutputStream codedOutputStream, Glob data) throws IOException;

            int getFieldNumber();
        }

        private static class NullWrite implements Write {
            static NullWrite nullWrite = new NullWrite();

            public void write(CodedOutputStream codedOutputStream, Glob data) {
            }

            public int getFieldNumber() {
                return 0;
            }
        }

        public static class FieldWithNumber {
            private final Write[] writes;

            public FieldWithNumber(Write[] writes) {
                this.writes = writes;
            }
        }

        public static class IntWrite implements Write {
            private final int index;
            private final IntegerField field;

            public IntWrite(Integer index, IntegerField field) {
                this.index = index;
                this.field = field;
            }

            public void write(CodedOutputStream codedOutputStream, Glob data) throws IOException {
                if (data.isSet(field)) {
                    Integer value = data.get(field);
                    if (value == null) {
                        codedOutputStream.writeNull(index);
                    } else {
                        codedOutputStream.writeInt32(index, value);
                    }
                }
            }

            public int getFieldNumber() {
                return index;
            }
        }

        private class GlobWrite implements Write {
            private int index;
            private GlobField field;

            public GlobWrite(int index, GlobField field) {
                this.index = index;
                this.field = field;
            }

            public void write(CodedOutputStream codedOutputStream, Glob data) throws IOException {
                if (!data.isSet(field)) {
                    return;
                }
                Glob glob = data.get(field);
                if (glob == null) {
                    codedOutputStream.writeNull(index);
                } else {
                    codedOutputStream.writeGlob(index);
                    GlobWriter.this.write(glob);
                }
            }

            public int getFieldNumber() {
                return index;
            }
        }
    }
}
