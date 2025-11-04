package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.DoubleArrayField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetDoubleArrayAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class DoubleArrayFieldReader implements FieldReader {
    private final int fieldNumber;
    private final DoubleArrayField field;
    private final GlobSetDoubleArrayAccessor setAccessor;

    public DoubleArrayFieldReader(int fieldNumber, DoubleArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        setAccessor = field.getGlobType().getSetAccessor(field);
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.DOUBLE_ARRAY:
                setAccessor.set(data, inputStream.readDoubleArray());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
