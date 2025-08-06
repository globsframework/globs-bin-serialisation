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
import org.globsframework.core.utils.exceptions.InvalidParameter;

import java.lang.reflect.Field;

public class UnionType {
    public static final GlobType TYPE;

    public static final Key UNIQUE_KEY;

    @Target(ChoiceType.class)
    public static GlobArrayField mapping;

    static {
        final GlobTypeBuilder globTypeBuilder = GlobTypeBuilderFactory.create("Union");
        TYPE = globTypeBuilder.unCompleteType();
        mapping = globTypeBuilder.declareGlobArrayField("mapping", ChoiceType.TYPE);
        globTypeBuilder.complete();
        UNIQUE_KEY = KeyBuilder.newEmptyKey(TYPE);
        globTypeBuilder.register(GlobCreateFromAnnotation.class, annotation -> {
            final UnionType_.ChoiceType_[] value = ((UnionType_) annotation).value();
            Glob[] choices = new Glob[value.length];
            for (int i = 0; i < choices.length; i++) {
                choices[i] = readChoice(value[i]);
            }
            return TYPE.instantiate()
                    .set(mapping, choices);
        });
    }

    public static Glob readChoice(UnionType_.ChoiceType_ choiceType){
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

        static {
            final GlobTypeBuilder globTypeBuilder = GlobTypeBuilderFactory.create("Choice");
            TYPE = globTypeBuilder.unCompleteType();
            index = globTypeBuilder.declareIntegerField("index");
            typeName = globTypeBuilder.declareStringField("type");
            globTypeBuilder.complete();
        }
    }
}
