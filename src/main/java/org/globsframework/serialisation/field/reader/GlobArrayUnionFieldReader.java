package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.core.metamodel.fields.GlobArrayUnionField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.globaccessor.set.GlobSetGlobArrayAccessor;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;
import org.globsframework.serialisation.model.UnionType;
import org.globsframework.serialisation.stream.CodedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.Collection;

public class GlobArrayUnionFieldReader implements FieldReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobArrayUnionFieldReader.class);
    private final Integer fieldNumber;
    private final GlobArrayUnionField field;
    private final GlobUnionFieldReader.TypeWithReader[] types;
    private final GlobSetGlobArrayAccessor setAccessor;

    public GlobArrayUnionFieldReader(Integer fieldNumber, GlobArrayUnionField field, GlobTypeFieldReadersFactory globTypeFieldReadersFactory) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        setAccessor = field.getGlobType().getSetAccessor(field);
        types = initTypesByIndex(field, field.getTargetTypes(), globTypeFieldReadersFactory);
    }

    public static GlobUnionFieldReader.TypeWithReader[] initTypesByIndex(Field field, Collection<GlobType> targetTypes,
                                                                         GlobTypeFieldReadersFactory globTypeFieldReadersFactory) {
        final GlobUnionFieldReader.TypeWithReader[] types;
        final Glob annotation = field.getAnnotation(UnionType.UNIQUE_KEY);
        final Glob[] mappings = annotation.getOrEmpty(UnionType.mapping);
        int max = 0;
        for (int i = 0; i < mappings.length; i++) {
            max = Math.max(max, mappings[i].get(UnionType.ChoiceType.index));
        }
        types = new GlobUnionFieldReader.TypeWithReader[max + 1];
        for (int i = 0; i < mappings.length; i++) {
            boolean found = false;
            for (GlobType targetType : targetTypes) {
                if (targetType.getName().equals(mappings[i].get(UnionType.ChoiceType.typeName))) {
                    types[mappings[i].get(UnionType.ChoiceType.index)] =
                            new GlobUnionFieldReader.TypeWithReader(targetType, globTypeFieldReadersFactory.create(targetType));
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new InvalidParameterException("Type " + mappings[i].get(UnionType.ChoiceType.typeName) + " not found");
            }
        }
        for (int i = 0; i < types.length; i++) {
            if (types[i] == null) {
                types[i] = new GlobUnionFieldReader.TypeWithReader(null, null);
            }
        }
        return types;
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                setAccessor.set(data, null);
                break;
            case WireConstants.Type.GLOB_UNION_ARRAY:
                int size = inputStream.readInt();
                Glob[] globs = new Glob[size];
                for (int index = 0; index < size; index++) {
                    int typeIndex = inputStream.readInt();
                    if (typeIndex != -1) {
                        final Glob glob = inputStream.readGlob(typeIndex >= types.length ? null : types[typeIndex].type(), types[typeIndex].readers());
                        globs[index] = glob;
                    }
                }
                setAccessor.set(data, globs);
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
