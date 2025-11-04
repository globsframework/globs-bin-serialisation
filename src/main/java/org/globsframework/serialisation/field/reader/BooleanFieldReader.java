package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.BooleanField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetBooleanAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class BooleanFieldReader implements FieldReader {
    private final int fieldNumber;
    private final BooleanField field;
    private final GlobSetBooleanAccessor setAccessor;

    public BooleanFieldReader(int fieldNumber, BooleanField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        setAccessor = field.getGlobType().getSetAccessor(field);
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.BOOLEAN:
                setAccessor.set(data, inputStream.readBoolean());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
