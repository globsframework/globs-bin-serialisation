package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.BytesField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetBytesAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public record BytesFieldReader(int fieldNumber, BytesField field, GlobSetBytesAccessor setAccessor) implements FieldReader {

    public BytesFieldReader(int fieldNumber, BytesField field) {
        this(fieldNumber,
                field,
                field.getGlobType().getSetAccessor(field));
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

    /** The same read, driven by a ToGlobCaller : the CallAt has just read the tag. */
    public void call(MutableGlob data, CodedInputStream inputStream, Void ignored, Void alsoIgnored) {
        read(data, inputStream.lastTag(), inputStream.lastWireType(), inputStream);
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
