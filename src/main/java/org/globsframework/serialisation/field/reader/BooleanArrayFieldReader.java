package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.BooleanArrayField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetBooleanArrayAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public record BooleanArrayFieldReader(int fieldNumber, BooleanArrayField field, GlobSetBooleanArrayAccessor setAccessor) implements FieldReader {

    public BooleanArrayFieldReader(int fieldNumber, BooleanArrayField field) {
        this(fieldNumber,
                field,
                field.getGlobType().getSetAccessor(field));
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

    /** The same read, driven by a GeneratedCallerWrite : the CallAt has just read the tag. */
    public void call(MutableGlob data, CodedInputStream inputStream, Void ignored, Void alsoIgnored) {
        read(data, inputStream.lastTag(), inputStream.lastWireType(), inputStream);
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
