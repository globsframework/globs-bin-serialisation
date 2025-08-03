package org.globsframework.serialisation.glob;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.MutableGlob;

public interface GlobInstantiator {
    MutableGlob newGlob(GlobType globType);
}
