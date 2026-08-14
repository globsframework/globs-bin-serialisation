package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.DateField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetDateAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public record DateFieldReader(int fieldNumber, DateField field, GlobSetDateAccessor setAccessor) implements FieldReader {

    public DateFieldReader(int fieldNumber, DateField field) {
        this(fieldNumber,
                field,
                field.getGlobType().getSetAccessor(field));
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.DATE:
                setAccessor.set(data, inputStream.readLocalDate());
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
