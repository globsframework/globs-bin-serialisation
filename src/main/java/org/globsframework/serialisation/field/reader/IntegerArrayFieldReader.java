package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.fields.IntegerArrayField;
import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class IntegerArrayFieldReader implements FieldReader {
    private final int fieldNumber;
    private final IntegerArrayField field;

    public IntegerArrayFieldReader(int fieldNumber, IntegerArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.INT_ARRAY:
                data.set(field, inputStream.readIntArray());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
