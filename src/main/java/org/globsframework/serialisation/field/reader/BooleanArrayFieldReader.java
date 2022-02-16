package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.fields.BooleanArrayField;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class BooleanArrayFieldReader implements FieldReader {
    private int fieldNumber;
    private final BooleanArrayField field;

    public BooleanArrayFieldReader(int fieldNumber, BooleanArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.BOOLEAN_ARRAY:
                data.set(field, inputStream.readBooleanArray());
                break;
            default:
                throw new RuntimeException("For " + field.getName() + " unexpected type " + tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
