package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.GlobArrayField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetGlobArrayAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobArrayFieldReader implements FieldReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobArrayFieldReader.class);
    private final Integer fieldNumber;
    private final GlobArrayField field;
    private final GlobType targetType;
    private final GlobSetGlobArrayAccessor setAccessor;

    public GlobArrayFieldReader(Integer fieldNumber, GlobArrayField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        setAccessor = field.getGlobType().getSetAccessor(field);
        targetType = field.getTargetType();
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL -> setAccessor.set(data, null);
            case WireConstants.Type.GLOB_ARRAY -> {
                int size = inputStream.readInt();
                Glob[] globs = new Glob[size];
                for (int index = 0; index < size; index++) {
                    final Glob glob = inputStream.readGlob(targetType);
                    globs[index] = glob;
                }
                setAccessor.set(data, globs);
            }
            default -> {
                String message = "For " + field.getName() + " unexpected type " + tagWireType;
                LOGGER.error(message);
                inputStream.skipField(tag);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
