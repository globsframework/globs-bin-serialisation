package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.GlobArrayField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetGlobArrayAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;
import org.globsframework.serialisation.stream.CodedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record GlobArrayFieldReader(Integer fieldNumber, GlobArrayField<?> field, GlobType targetType, GlobSetGlobArrayAccessor setAccessor, GlobTypeFieldReaders globTypeFieldReaders) implements FieldReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobArrayFieldReader.class);

    public GlobArrayFieldReader(Integer fieldNumber, GlobArrayField<?> field, GlobTypeFieldReadersFactory globTypeFieldReadersFactory) {
        this(fieldNumber,
                field,
                field.getTargetType(),
                field.getGlobType().getSetAccessor(field),
                globTypeFieldReadersFactory.create(field.getTargetType()));
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL -> setAccessor.set(data, null);
            case WireConstants.Type.GLOB_ARRAY -> {
                int size = inputStream.readInt();
                Glob[] globs = new Glob[size];
                for (int index = 0; index < size; index++) {
                    final Glob glob = inputStream.readGlob(targetType, globTypeFieldReaders);
                    globs[index] = glob;
                }
                setAccessor.set(data, globs);
            }
            default -> {
                String message = "For " + field.getName() + " unexpected type " + tagWireType;
                LOGGER.error(message);
                inputStream.skipFieldFromWireType(tagWireType);
            }
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
