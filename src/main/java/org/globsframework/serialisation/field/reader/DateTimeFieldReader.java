package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.DateTimeField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class DateTimeFieldReader implements FieldReader {
    private final int fieldNumber;
    private final DateTimeField field;

    public DateTimeFieldReader(int fieldNumber, DateTimeField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.DATE_TIME:
                data.set(field, inputStream.readZonedDateTime());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
