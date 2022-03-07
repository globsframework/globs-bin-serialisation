package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.BigDecimalArrayField;
import org.globsframework.model.FieldValuesAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.math.BigDecimal;

public class BigDecimalArrayFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final BigDecimalArrayField field;

    public BigDecimalArrayFieldWriter(Integer fieldNumber, BigDecimalArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data) {
        if (data.isSet(field)) {
            BigDecimal[] value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(fieldNumber);
            } else {
                codedOutputStream.writeBigDecimalArray(fieldNumber, value);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
