package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.fields.GlobUnionField;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.BinReader;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GlobUnionFieldReader implements FieldReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobUnionFieldReader.class);

    private final BinReader binReader;
    private final Integer fieldNumber;
    private final GlobUnionField field;

    public GlobUnionFieldReader(BinReader binReader, Integer fieldNumber, GlobUnionField field) {
        this.binReader = binReader;
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) throws IOException {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.GLOB_UNION:
                data.set(field, binReader.read(field.getTargetTypes()));
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
