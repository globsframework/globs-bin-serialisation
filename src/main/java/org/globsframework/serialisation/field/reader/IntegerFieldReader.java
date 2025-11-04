package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetIntAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class IntegerFieldReader implements FieldReader {
    private final int fieldNumber;
    private final IntegerField field;
    private final GlobSetIntAccessor setAccessor;

    public IntegerFieldReader(int fieldNumber, IntegerField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        setAccessor = field.getGlobType().getSetAccessor(field);
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.INT:
                setAccessor.set(data, inputStream.readInt());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
