package org.globsframework.serialisation.model;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.GlobTypeBuilderFactory;
import org.globsframework.core.metamodel.fields.GlobArrayField;
import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class UnionType {
    public static final GlobType TYPE;

    public static final Key UNIQUE_KEY;

    public static GlobArrayField<ChoiceType> mapping;

    static {
        final GlobTypeBuilder globTypeBuilder = GlobTypeBuilderFactory.create("Union");
        mapping = globTypeBuilder.declareGlobArrayField("mapping", () -> ChoiceType.TYPE);
        TYPE = globTypeBuilder.build();
        UNIQUE_KEY = KeyBuilder.newEmptyKey(TYPE);
    }


    static public Glob create(Glob... choices) {
        return TYPE.instantiate()
                .set(mapping, choices);
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
