package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.StringArrayField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetStringArrayAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class StringArrayFieldReader implements FieldReader {
    private final int fieldNumber;
    private final StringArrayField field;
    private final GlobSetStringArrayAccessor setAccessor;

    public StringArrayFieldReader(int fieldNumber, StringArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        setAccessor = field.getGlobType().getSetAccessor(field);
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.STRING_ARRAY:
                setAccessor.set(data, inputStream.readStringArray());
                break;
            default:
                defaultReadCase(field.getName(), tagWireType);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

}
