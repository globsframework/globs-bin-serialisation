package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.IntegerArrayField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetIntArrayAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class IntegerArrayFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetIntArrayAccessor getAccessor;

    public IntegerArrayFieldWriter(Integer fieldNumber, IntegerArrayField field) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        int[] value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeIntArray(fieldNumber, value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
