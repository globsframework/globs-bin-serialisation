package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class IntFieldWriter implements FieldWriter {
    private final int index;
    private final IntegerField field;

    public IntFieldWriter(Integer index, IntegerField field) {
        this.index = index;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        if (data.isSet(field)) {
            Integer value = data.get(field);
            if (value == null) {
                codedOutputStream.writeNull(index);
            } else {
                codedOutputStream.writeInt32(index, value);
            }
        }
    }

    public int getFieldNumber() {
        return index;
    }
}
