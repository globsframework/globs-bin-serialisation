package org.globsframework.serialisation.model;

import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.metamodel.annotations.InitUniqueKey;
import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.model.Key;

public class FieldNumberAnnotationType {
    public static GlobType TYPE;

    public static IntegerField fieldNumber;

    @InitUniqueKey
    public static Key key;

    static {
        GlobTypeLoaderFactory.create(FieldNumberAnnotationType.class)
                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                        .set(fieldNumber, ((FieldNumber) annotation).value()))
                .load();
    }
}
