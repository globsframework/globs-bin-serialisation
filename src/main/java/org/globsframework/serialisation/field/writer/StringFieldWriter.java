package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.StringField;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class StringFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final StringField field;

    public StringFieldWriter(Integer fieldNumber, StringField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        if (data.isSet(field)) {
            String value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(fieldNumber);
            } else {
                codedOutputStream.writeUtf8String(fieldNumber, value);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
