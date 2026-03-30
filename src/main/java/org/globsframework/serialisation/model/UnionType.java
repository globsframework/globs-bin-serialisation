package org.globsframework.serialisation.model;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.GlobTypeBuilderFactory;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.Target;
import org.globsframework.core.metamodel.fields.GlobArrayField;
import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.utils.exceptions.InvalidParameter;

import java.lang.reflect.Field;

public class UnionType {
    public static final GlobType TYPE;

    public static final Key UNIQUE_KEY;

    @Target(ChoiceType.class)
    public static GlobArrayField mapping;

    static {
        final GlobTypeBuilder globTypeBuilder = GlobTypeBuilderFactory.create("Union");
        mapping = globTypeBuilder.declareGlobArrayField("mapping", () -> ChoiceType.TYPE);
        globTypeBuilder.register(GlobCreateFromAnnotation.class, annotation -> createAnnotation((UnionType_) annotation));
        TYPE = globTypeBuilder.build();
        UNIQUE_KEY = KeyBuilder.newEmptyKey(TYPE);
    }


    static public Glob create(Glob... choices) {
        return TYPE.instantiate()
                .set(mapping, choices);
    }

    private static MutableGlob createAnnotation(UnionType_ annotation) {
        final UnionType_.ChoiceType_[] value = annotation.value();
        Glob[] choices = new Glob[value.length];
        for (int i = 0; i < choices.length; i++) {
            choices[i] = readChoice(value[i]);
        }
        return TYPE.instantiate()
                .set(mapping, choices);
    }

    public static Glob readChoice(UnionType_.ChoiceType_ choiceType) {
        final Class<?> aClass = choiceType.value();
        final Field type;
        GlobType globType;
        try {
            type = aClass.getField("TYPE");
            globType = (GlobType) type.get(null);
            if (globType == null) {
                throw new InvalidParameter("In " + aClass.getName() + " the field 'TYPE' is not initialised.");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Fail to find a field TYPE in class " + aClass.getName() + " to extract glob type name.", e);
        }
        return ChoiceType.TYPE.instantiate()
                .set(ChoiceType.index, choiceType.index())
                .set(ChoiceType.typeName, globType.getName());

    }

    public static class ChoiceType {
        public static final GlobType TYPE;

        public static final IntegerField index;

        public static final StringField typeName;

        public static Glob create(String typeName, int index) {
            return TYPE.instantiate()
                    .set(ChoiceType.index, index)
                    .set(ChoiceType.typeName, typeName);
        }

        static {
            final GlobTypeBuilder globTypeBuilder = GlobTypeBuilderFactory.create("Choice");
            index = globTypeBuilder.declareIntegerField("index");
            typeName = globTypeBuilder.declareStringField("type");
            TYPE = globTypeBuilder.build();
        }
    }
}
