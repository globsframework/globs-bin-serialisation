package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.IntegerArrayField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetIntArrayAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class IntegerArrayFieldReader implements FieldReader {
    private final int fieldNumber;
    private final IntegerArrayField field;
    private final GlobSetIntArrayAccessor setAccessor;

    public IntegerArrayFieldReader(int fieldNumber, IntegerArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        setAccessor = field.getGlobType().getSetAccessor(field);
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.INT_ARRAY:
                setAccessor.set(data, inputStream.readIntArray());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
