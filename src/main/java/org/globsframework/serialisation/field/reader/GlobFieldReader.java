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

public record GlobFieldReader(Integer fieldNumber, GlobField<?> field, GlobType targetType, GlobSetGlobAccessor setAccessor, GlobTypeFieldReaders globTypeFieldReaders) implements FieldReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobFieldReader.class);

    public GlobFieldReader(Integer fieldNumber, GlobField<?> field, GlobTypeFieldReadersFactory globTypeFieldReadersFactory) {
        this(fieldNumber,
                field,
                field.getTargetType(),
                field.getGlobType().getSetAccessor(field),
                globTypeFieldReadersFactory.create(field.getTargetType()));
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
                inputStream.skipFieldFromWireType(tagWireType);
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
