package org.globsframework.serialisation.model;

import org.globsframework.metamodel.GlobType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@java.lang.annotation.Target({ElementType.FIELD})
public @interface FieldNumber {
    int value();

    GlobType TYPE = FieldNumberAnnotationType.TYPE;
}
