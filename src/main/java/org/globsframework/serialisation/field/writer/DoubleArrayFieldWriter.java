package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.DoubleArrayField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetDoubleArrayAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public record DoubleArrayFieldWriter(int fieldNumber, GlobGetDoubleArrayAccessor getAccessor) implements FieldWriter {

    public DoubleArrayFieldWriter(Integer fieldNumber, DoubleArrayField field) {
        this(fieldNumber,
                (GlobGetDoubleArrayAccessor) field.getGlobType().getGetAccessor(field));
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        double[] value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeDoubleArray(fieldNumber, value);
        }
    }

    /** The same thing driven by a GeneratedFunctionCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeDoubleArray(fieldNumber, (double[]) value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
