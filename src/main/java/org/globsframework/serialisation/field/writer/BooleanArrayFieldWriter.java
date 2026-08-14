package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.BooleanArrayField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetBooleanArrayAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public record BooleanArrayFieldWriter(int fieldNumber, GlobGetBooleanArrayAccessor getAccessor) implements FieldWriter {

    public BooleanArrayFieldWriter(Integer fieldNumber, BooleanArrayField field) {
        this(fieldNumber,
                (GlobGetBooleanArrayAccessor) field.getGlobType().getGetAccessor(field));
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        boolean[] value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBooleanArray(fieldNumber, value);
        }
    }

    /** The same thing driven by a GeneratedFunctionCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBooleanArray(fieldNumber, (boolean[]) value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
