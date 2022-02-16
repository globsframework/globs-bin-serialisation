package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.BooleanArrayField;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class BooleanArrayFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final BooleanArrayField field;

    public BooleanArrayFieldWriter(Integer fieldNumber, BooleanArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        if (data.isSet(field)) {
            boolean[] value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(fieldNumber);
            } else {
                codedOutputStream.writeBooleanArray(fieldNumber, value);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
