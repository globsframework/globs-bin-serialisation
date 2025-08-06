package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.core.metamodel.fields.GlobArrayUnionField;
import org.globsframework.core.model.FieldValuesAccessor;
import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.model.UnionType;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GlobArrayUnionFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobArrayUnionField field;
    private final Map<GlobType, Integer> types;

    public GlobArrayUnionFieldWriter(int fieldNumber, GlobArrayUnionField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        types = initTypesByIndex(field, field.getTargetTypes());
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data, BinWriter binWriter) {
        if (!data.isSet(field)) {
            return;
        }
        Glob[] globs = data.get(field);
        if (globs == null) {
            codedOutputStream.writeNull(fieldNumber);
        } else {
            codedOutputStream.writeGlobArrayUnion(fieldNumber, globs.length);
            for (Glob glob : globs) {
                if (glob == null) {
                    codedOutputStream.writeInt(-1);
                }
                else {
                    final Integer index = types.get(glob.getType());
                    if (index == null) {
                        throw new RuntimeException("Unsupported glob type " + glob.getType() + " in " + field.getFullName());
                    }
                    codedOutputStream.writeInt(index);
                    binWriter.write(glob);
                }
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

    public static Map<GlobType, Integer> initTypesByIndex(Field field, Collection<GlobType> targetTypes) {
        final Glob annotation = field.getAnnotation(UnionType.UNIQUE_KEY);
        final Glob[] mappings = annotation.getOrEmpty(UnionType.mapping);
        Map<GlobType, Integer> typeMap = new HashMap<>();
        for (int i = 0; i < mappings.length; i++) {
            boolean found = false;
            for (GlobType targetType : targetTypes) {
                if (targetType.getName().equals(mappings[i].get(UnionType.ChoiceType.typeName))) {
                    typeMap.put(targetType, mappings[i].get(UnionType.ChoiceType.index));
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
