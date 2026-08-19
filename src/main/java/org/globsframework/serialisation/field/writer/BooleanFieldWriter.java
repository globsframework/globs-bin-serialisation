package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.BooleanField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetBooleanAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public record BooleanFieldWriter(int fieldNumber, GlobGetBooleanAccessor getAccessor) implements FieldWriter {

    public BooleanFieldWriter(Integer fieldNumber, BooleanField field) {
        this(fieldNumber,
                (GlobGetBooleanAccessor) field.getGlobType().getGetAccessor(field));
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        final Boolean value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBoolean(fieldNumber, value);
        }
    }

    /** The same thing driven by a FromGlobCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBoolean(fieldNumber, (Boolean) value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
