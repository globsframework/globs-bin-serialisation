package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetIntAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public record IntegerFieldWriter(int fieldNumber, GlobGetIntAccessor getAccessor) implements FieldWriter {

    public IntegerFieldWriter(Integer fieldNumber, IntegerField field) {
        this(fieldNumber,
                (GlobGetIntAccessor) field.getGlobType().getGetAccessor(field));
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

    /** The same thing driven by a GeneratedFunctionCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeInt32(fieldNumber, (Integer) value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
