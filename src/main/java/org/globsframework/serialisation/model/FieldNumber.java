package org.globsframework.serialisation.model;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class FieldNumber {
    public static final GlobType TYPE;

    public static final IntegerField fieldNumber;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("FieldNumber");
        fieldNumber = typeBuilder.declareIntegerField("fieldNumber");
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> create(((FieldNumber_) annotation).value()));
        TYPE = typeBuilder.build();
        KEY = KeyBuilder.newEmptyKey(TYPE);
    }

    public static Glob create(int index) {
        return TYPE.instantiate().set(fieldNumber, index);
    }
}
