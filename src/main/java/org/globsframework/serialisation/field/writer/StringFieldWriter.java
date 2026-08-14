package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetStringAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public record StringFieldWriter(int fieldNumber, GlobGetStringAccessor getAccessor) implements FieldWriter {

    public StringFieldWriter(Integer fieldNumber, StringField field) {
        this(fieldNumber,
                (GlobGetStringAccessor) field.getGlobType().getGetAccessor(field));
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        String value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeUtf8String(fieldNumber, value);
        }
    }

    /** The same thing driven by a GeneratedFunctionCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeUtf8String(fieldNumber, (String) value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
