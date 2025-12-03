package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.core.metamodel.fields.GlobArrayUnionField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetGlobArrayAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;
import org.globsframework.serialisation.model.UnionType;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GlobArrayUnionFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobArrayUnionField field;
    private final Map<GlobType, GlobUnionFieldWriter.IndiceWithWriter> types;
    private final GlobGetGlobArrayAccessor getAccessor;

    public GlobArrayUnionFieldWriter(int fieldNumber, GlobArrayUnionField field, GlobTypeFieldWritersFactory fieldWritersFactory) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        types = initTypesByIndex(field, field.getTargetTypes(), fieldWritersFactory);
        getAccessor = field.getGlobType().getGetAccessor(field);
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        Glob[] globs = getAccessor.get(data);
        if (globs == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeGlobArrayUnion(fieldNumber, globs.length);
            for (Glob glob : globs) {
                if (glob == null) {
                    codedOutputStream.writeInt(-1);
                } else {
                    final GlobUnionFieldWriter.IndiceWithWriter index = types.get(glob.getType());
                    if (index == null) {
                        throw new RuntimeException("Unsupported glob type " + glob.getType() + " in " + field.getFullName());
                    }
                    codedOutputStream.writeInt(index.indice());
                    index.fieldWriters().write(codedOutputStream, glob);
                }
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

    public static Map<GlobType, GlobUnionFieldWriter.IndiceWithWriter> initTypesByIndex(Field field, Collection<GlobType> targetTypes, GlobTypeFieldWritersFactory fieldWritersFactory) {
        final Glob annotation = field.getAnnotation(UnionType.UNIQUE_KEY);
        final Glob[] mappings = annotation.getOrEmpty(UnionType.mapping);
        Map<GlobType, GlobUnionFieldWriter.IndiceWithWriter> typeMap = new HashMap<>();
        for (int i = 0; i < mappings.length; i++) {
            boolean found = false;
            for (GlobType targetType : targetTypes) {
                if (targetType.getName().equals(mappings[i].get(UnionType.ChoiceType.typeName))) {
                    typeMap.put(targetType,
                            new GlobUnionFieldWriter.IndiceWithWriter(mappings[i].get(UnionType.ChoiceType.index), fieldWritersFactory.create(targetType)));
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new InvalidParameterException("Type " + mappings[i].get(UnionType.ChoiceType.typeName) + " not found");
            }
        }
        return typeMap;
    }

}
