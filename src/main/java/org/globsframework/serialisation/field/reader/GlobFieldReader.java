package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.GlobTypeResolver;
import org.globsframework.metamodel.fields.GlobField;
import org.globsframework.model.Glob;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.BinReader;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class GlobFieldReader implements FieldReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobFieldReader.class);
    private final Integer fieldNumber;
    private final GlobField field;
    private final GlobTypeResolver resolver;

    public GlobFieldReader(Integer fieldNumber, GlobField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        resolver = GlobTypeResolver.from(field.getGlobType());
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.GLOB:
                inputStream
                        .readGlob(resolver)
                        .ifPresent(glob -> data.set(field, glob));
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
