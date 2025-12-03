package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetIntAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class IntegerFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetIntAccessor getAccessor;

    public IntegerFieldWriter(Integer fieldNumber, IntegerField field) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        Integer value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeInt32(fieldNumber, value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
