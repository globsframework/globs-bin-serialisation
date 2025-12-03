package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.GlobField;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetGlobAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;
import org.globsframework.serialisation.stream.CodedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobFieldReader implements FieldReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobFieldReader.class);
    private final Integer fieldNumber;
    private final GlobField field;
    private final GlobType targetType;
    private final GlobSetGlobAccessor setAccessor;
    private final GlobTypeFieldReaders globTypeFieldReaders;

    public GlobFieldReader(Integer fieldNumber, GlobField field, GlobTypeFieldReadersFactory globTypeFieldReadersFactory) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        setAccessor = field.getGlobType().getSetAccessor(field);
        targetType = field.getTargetType();
        globTypeFieldReaders = globTypeFieldReadersFactory.create(targetType);
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.GLOB:
                setAccessor.set(data, inputStream.readGlob(targetType, globTypeFieldReaders));
                break;
            default:
                String message = "For " + field.getName() + " unexpected type " + tagWireType;
                LOGGER.error(message);
                inputStream.skipField(tag);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
