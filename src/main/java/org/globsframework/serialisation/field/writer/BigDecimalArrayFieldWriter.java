package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.BigDecimalArrayField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetBigDecimalArrayAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.math.BigDecimal;

public record BigDecimalArrayFieldWriter(int fieldNumber, GlobGetBigDecimalArrayAccessor getAccessor) implements FieldWriter {

    public BigDecimalArrayFieldWriter(Integer fieldNumber, BigDecimalArrayField field) {
        this(fieldNumber,
                (GlobGetBigDecimalArrayAccessor) field.getGlobType().getGetAccessor(field));
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        BigDecimal[] value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBigDecimalArray(fieldNumber, value);
        }
    }

    /** The same thing driven by a FromGlobCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBigDecimalArray(fieldNumber, (BigDecimal[]) value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
