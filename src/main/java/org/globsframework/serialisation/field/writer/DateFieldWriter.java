package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.DateField;
import org.globsframework.core.model.FieldValuesAccessor;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.time.LocalDate;

public class DateFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final DateField field;

    public DateFieldWriter(Integer fieldNumber, DateField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data, BinWriter binWriter) {
        if (data.isSet(field)) {
            LocalDate value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(fieldNumber);
            } else {
                codedOutputStream.writeLocalDate(fieldNumber, value);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
