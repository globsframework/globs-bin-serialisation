package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.GlobTypeResolver;
import org.globsframework.metamodel.fields.GlobArrayField;
import org.globsframework.model.Glob;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.BinReader;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GlobArrayFieldReader implements FieldReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobArrayFieldReader.class);
    private final Integer fieldNumber;
    private final GlobArrayField field;
    private final GlobTypeResolver resolver;

    public GlobArrayFieldReader(Integer fieldNumber, GlobArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        resolver = GlobTypeResolver.from(field.getGlobType());
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.GLOB_ARRAY:
                List<Glob> globs = new ArrayList<>();
                int size = inputStream.readInt();
                for (int index = 0; index < size; index++) {
                    inputStream
                            .readGlob(resolver)
                            .ifPresent(globs::add);
                }
                data.set(field, globs.toArray(new Glob[globs.size()]));
                break;
            default:
                String message = "For " + field.getName() + " unexpected type " + tagWireType;
                LOGGER.error(message);
                inputStream.skipField(tag);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
