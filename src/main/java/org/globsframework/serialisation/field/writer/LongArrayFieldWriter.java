package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.LongArrayField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetLongArrayAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public final class LongArrayFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetLongArrayAccessor getAccessor;

    public LongArrayFieldWriter(Integer fieldNumber, LongArrayField field) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        long[] value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeLongArray(fieldNumber, value);
        }
    }

    /** The same thing driven by a GeneratedFunctionCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeLongArray(fieldNumber, (long[]) value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
