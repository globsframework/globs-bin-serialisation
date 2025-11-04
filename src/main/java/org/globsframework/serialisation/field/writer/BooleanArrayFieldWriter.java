package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.BooleanArrayField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetBooleanArrayAccessor;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class BooleanArrayFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetBooleanArrayAccessor getAccessor;

    public BooleanArrayFieldWriter(Integer fieldNumber, BooleanArrayField field) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
    }

    public void write(CodedOutputStream codedOutputStream, Glob data, BinWriter binWriter) {
        boolean[] value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBooleanArray(fieldNumber, value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
