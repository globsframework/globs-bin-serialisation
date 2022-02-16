package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.fields.LongArrayField;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class LongArrayFieldReader implements FieldReader {
    private final int fieldNumber;
    private final LongArrayField field;

    public LongArrayFieldReader(int fieldNumber, LongArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.LONG_ARRAY:
                data.set(field, inputStream.readLongArray());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
