package org.globsframework.serialisation.model;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeLoaderFactory;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.Key;

public class FieldNumber {
    public static GlobType TYPE;

    public static IntegerField fieldNumber;

    @InitUniqueKey
    public static Key key;

    static {
        GlobTypeLoaderFactory.create(FieldNumber.class, "FieldNumber")
                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                        .set(fieldNumber, ((FieldNumber_) annotation).value()))
                .load();
    }

    public static Glob create(int index) {
        return TYPE.instantiate().set(fieldNumber, index);
    }
}
