package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.fields.StringField;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class StringFieldReader implements FieldReader {
    private int fieldNumber;
    private final StringField field;

    public StringFieldReader(int fieldNumber, StringField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.STRING:
                data.set(field, inputStream.readUtf8String());
                break;
            default:
                throw new RuntimeException("For " + field.getName() + " unexpected type " + tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
