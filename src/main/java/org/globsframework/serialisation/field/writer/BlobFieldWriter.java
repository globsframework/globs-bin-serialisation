package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.BlobField;
import org.globsframework.core.model.FieldValuesAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class BlobFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final BlobField field;

    public BlobFieldWriter(Integer fieldNumber, BlobField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data) {
        if (data.isSet(field)) {
            byte[] value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(fieldNumber);
            } else {
                codedOutputStream.writeBytes(fieldNumber, value);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
