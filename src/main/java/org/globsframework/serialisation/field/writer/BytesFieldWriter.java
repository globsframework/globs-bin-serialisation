package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.BytesField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetBytesAccessor;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class BytesFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetBytesAccessor getAccessor;

    public BytesFieldWriter(Integer fieldNumber, BytesField field) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
    }

    public void write(CodedOutputStream codedOutputStream, Glob data, BinWriter binWriter) {
        byte[] value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBytes(fieldNumber, value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
