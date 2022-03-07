package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.StringArrayField;
import org.globsframework.model.FieldValuesAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class StringArrayFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final StringArrayField field;

    public StringArrayFieldWriter(Integer fieldNumber, StringArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data) {
        if (data.isSet(field)) {
            String[] value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(fieldNumber);
            } else {
                codedOutputStream.writeStringArray(fieldNumber, value);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
