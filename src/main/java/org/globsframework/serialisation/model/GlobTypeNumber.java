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

public class GlobTypeNumber {
    public static final GlobType TYPE;

    public static final IntegerField globTypeNumber;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("GlobTypeNumber");
        TYPE = typeBuilder.unCompleteType();
        globTypeNumber = typeBuilder.declareIntegerField("globTypeNumber");
        typeBuilder.complete();
        KEY = KeyBuilder.newEmptyKey(TYPE);
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                .set(globTypeNumber, ((GlobTypeNumber_) annotation).value()));

//        GlobTypeLoaderFactory.create(FieldNumber.class, "FieldNumber")
//                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
//                        .set(fieldNumber, ((FieldNumber_) annotation).value()))
//                .load();
    }

    public static Glob create(int index) {
        return TYPE.instantiate().set(globTypeNumber, index);
    }
}
