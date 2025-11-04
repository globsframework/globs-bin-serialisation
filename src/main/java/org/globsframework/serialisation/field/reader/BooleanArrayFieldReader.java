package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.BooleanArrayField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetBooleanArrayAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class BooleanArrayFieldReader implements FieldReader {
    private final int fieldNumber;
    private final BooleanArrayField field;
    private final GlobSetBooleanArrayAccessor setAccessor;

    public BooleanArrayFieldReader(int fieldNumber, BooleanArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        setAccessor = field.getGlobType().getSetAccessor(field);
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.BOOLEAN_ARRAY:
                setAccessor.set(data, inputStream.readBooleanArray());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
