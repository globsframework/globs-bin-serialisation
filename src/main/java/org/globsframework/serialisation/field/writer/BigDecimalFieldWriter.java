package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.BigDecimalField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetBigDecimalAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.math.BigDecimal;

public class BigDecimalFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetBigDecimalAccessor getAccessor;

    public BigDecimalFieldWriter(Integer fieldNumber, BigDecimalField field) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
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

    public int getFieldNumber() {
        return fieldNumber;
    }
}
