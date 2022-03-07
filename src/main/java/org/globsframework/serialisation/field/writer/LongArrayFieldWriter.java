package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.LongArrayField;
import org.globsframework.model.FieldValuesAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class LongArrayFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final LongArrayField field;

    public LongArrayFieldWriter(Integer fieldNumber, LongArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data) {
        if (data.isSet(field)) {
            long[] value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(fieldNumber);
            } else {
                codedOutputStream.writeLongArray(fieldNumber, value);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
