package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.BigDecimalField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetBigDecimalAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.math.BigDecimal;

public record BigDecimalFieldWriter(int fieldNumber, GlobGetBigDecimalAccessor getAccessor) implements FieldWriter {

    public BigDecimalFieldWriter(Integer fieldNumber, BigDecimalField field) {
        this(fieldNumber,
                (GlobGetBigDecimalAccessor) field.getGlobType().getGetAccessor(field));
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        BigDecimal value = getAccessor.get(data);
        if (value == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBigDecimal(fieldNumber, value);
        }
    }

    /** The same thing driven by a GeneratedFunctionCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeBigDecimal(fieldNumber, (BigDecimal) value);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
