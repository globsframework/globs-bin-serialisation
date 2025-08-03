package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.GlobField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobFieldReader implements FieldReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobFieldReader.class);
    private final Integer fieldNumber;
    private final GlobField field;
    private final GlobTypeIndexResolver resolver;

    public GlobFieldReader(Integer fieldNumber, GlobField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        resolver = GlobTypeIndexResolver.from(field.getGlobType());
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.GLOB:
                data.set(field, inputStream.readGlob(resolver));
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
