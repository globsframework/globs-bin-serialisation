package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.fields.StringArrayField;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class StringArrayFieldReader implements FieldReader {
    private final int fieldNumber;
    private final StringArrayField field;

    public StringArrayFieldReader(int fieldNumber, StringArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.STRING_ARRAY:
                data.set(field, inputStream.readStringArray());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
