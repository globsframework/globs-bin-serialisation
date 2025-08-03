package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.LongField;
import org.globsframework.core.model.FieldValuesAccessor;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class LongFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final LongField field;

    public LongFieldWriter(Integer fieldNumber, LongField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data, BinWriter binWriter) {
        if (data.isSet(field)) {
            Long value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(fieldNumber);
            } else {
                codedOutputStream.writeLong(fieldNumber, value);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
