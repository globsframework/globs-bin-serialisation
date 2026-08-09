package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.DateField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetDateAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.time.LocalDate;

public final class DateFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetDateAccessor getAccessor;

    public DateFieldWriter(Integer fieldNumber, DateField field) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        LocalDate value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeLocalDate(fieldNumber, value);
        }
    }

    /** The same thing driven by a GeneratedFunctionCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeLocalDate(fieldNumber, (LocalDate) value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
