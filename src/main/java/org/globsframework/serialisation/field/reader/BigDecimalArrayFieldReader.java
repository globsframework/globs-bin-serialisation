package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.BigDecimalArrayField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetBigDecimalArrayAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public record BigDecimalArrayFieldReader(int fieldNumber, BigDecimalArrayField field, GlobSetBigDecimalArrayAccessor setAccessor) implements FieldReader {

    public BigDecimalArrayFieldReader(int fieldNumber, BigDecimalArrayField field) {
        this(fieldNumber,
                field,
                field.getGlobType().getSetAccessor(field));
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.BIG_DECIMAL_ARRAY:
                setAccessor.set(data, inputStream.readBigDecimalArray());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    /** The same read, driven by a ToGlobCaller : the CallAt has just read the tag. */
    public void call(MutableGlob data, CodedInputStream inputStream, Void ignored, Void alsoIgnored) {
        read(data, inputStream.lastTag(), inputStream.lastWireType(), inputStream);
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
