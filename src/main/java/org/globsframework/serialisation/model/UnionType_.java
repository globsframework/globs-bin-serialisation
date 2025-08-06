package org.globsframework.serialisation.model;

import org.globsframework.core.metamodel.GlobType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ElementType.FIELD})
public @interface UnionType_ {
    ChoiceType_[] value();

    GlobType TYPE = UnionType.TYPE;

    @interface ChoiceType_ {
        Class<?> value();
        int index();
    }
}
