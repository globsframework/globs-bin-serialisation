package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.BlobField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetBytesAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class BytesFieldReader implements FieldReader {
    private final int fieldNumber;
    private final BlobField field;
    private final GlobSetBytesAccessor setAccessor;

    public BytesFieldReader(int fieldNumber, BlobField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        setAccessor = field.getGlobType().getSetAccessor(field);
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.BYTES:
                setAccessor.set(data, inputStream.readBytes());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
