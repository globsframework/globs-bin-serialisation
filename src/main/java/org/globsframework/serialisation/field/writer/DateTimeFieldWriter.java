package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.DateTimeField;
import org.globsframework.core.model.FieldValuesAccessor;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.time.ZonedDateTime;

public class DateTimeFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final DateTimeField field;

    public DateTimeFieldWriter(Integer fieldNumber, DateTimeField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data, BinWriter binWriter) {
        if (data.isSet(field)) {
            ZonedDateTime value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(fieldNumber);
            } else {
                codedOutputStream.writeZonedDateTime(fieldNumber, value);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
