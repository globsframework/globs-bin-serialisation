package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.BigDecimalField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class BigDecimalFieldReader implements FieldReader {
    private final int fieldNumber;
    private final BigDecimalField field;

    public BigDecimalFieldReader(int fieldNumber, BigDecimalField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.BIG_DECIMAL:
                data.set(field, inputStream.readBigDecimal());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
