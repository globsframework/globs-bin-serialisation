package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.DateTimeField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetDateTimeAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class DateTimeFieldReader implements FieldReader {
    private final int fieldNumber;
    private final DateTimeField field;
    private final GlobSetDateTimeAccessor setAccessor;

    public DateTimeFieldReader(int fieldNumber, DateTimeField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        setAccessor = field.getGlobType().getSetAccessor(field);
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.DATE_TIME:
                setAccessor.set(data, inputStream.readZonedDateTime());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
