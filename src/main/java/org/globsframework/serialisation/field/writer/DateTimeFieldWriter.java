package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.DateTimeField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetDateTimeAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.time.ZonedDateTime;

public final class DateTimeFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetDateTimeAccessor getAccessor;

    public DateTimeFieldWriter(Integer fieldNumber, DateTimeField field) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        ZonedDateTime value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeZonedDateTime(fieldNumber, value);
        }
    }

    /** The same thing driven by a GeneratedFunctionCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeZonedDateTime(fieldNumber, (ZonedDateTime) value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
