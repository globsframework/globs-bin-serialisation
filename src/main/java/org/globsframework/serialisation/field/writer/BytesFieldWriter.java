package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.BytesField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetBytesAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public record BytesFieldWriter(int fieldNumber, GlobGetBytesAccessor getAccessor) implements FieldWriter {

    public BytesFieldWriter(Integer fieldNumber, BytesField field) {
        this(fieldNumber,
                (GlobGetBytesAccessor) field.getGlobType().getGetAccessor(field));
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        byte[] value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBytes(fieldNumber, value);
        }
    }

    /** The same thing driven by a GeneratedFunctionCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBytes(fieldNumber, (byte[]) value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
