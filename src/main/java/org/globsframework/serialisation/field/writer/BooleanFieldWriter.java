package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.BooleanField;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class BooleanFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final BooleanField field;

    public BooleanFieldWriter(Integer fieldNumber, BooleanField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        if (data.isSet(field)) {
            Boolean value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(fieldNumber);
            } else {
                codedOutputStream.writeBoolean(fieldNumber, value);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
