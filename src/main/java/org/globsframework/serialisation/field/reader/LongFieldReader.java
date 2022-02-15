package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.fields.LongField;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class LongFieldReader implements FieldReader {
    private int fieldNumber;
    private final LongField field;

    public LongFieldReader(int fieldNumber, LongField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.LONG:
                data.set(field, inputStream.readLong());
                break;
            default:
                throw new RuntimeException("For " + field.getName() + " unexpected type " + tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
