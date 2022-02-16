package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.fields.DoubleArrayField;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class DoubleArrayFieldReader implements FieldReader {
    private final int fieldNumber;
    private final DoubleArrayField field;

    public DoubleArrayFieldReader(int fieldNumber, DoubleArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.DOUBLE_ARRAY:
                data.set(field, inputStream.readDoubleArray());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
