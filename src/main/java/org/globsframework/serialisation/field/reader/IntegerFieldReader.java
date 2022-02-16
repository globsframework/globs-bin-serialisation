package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class IntegerFieldReader implements FieldReader {
    private final int fieldNumber;
    private final IntegerField field;

    public IntegerFieldReader(int fieldNumber, IntegerField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.INT:
                data.set(field, inputStream.readInt());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
