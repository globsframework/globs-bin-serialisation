package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.DoubleArrayField;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class DoubleArrayFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final DoubleArrayField field;

    public DoubleArrayFieldWriter(Integer fieldNumber, DoubleArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        if (data.isSet(field)) {
            double[] value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(fieldNumber);
            } else {
                codedOutputStream.writeDoubleArray(fieldNumber, value);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
