package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.utils.collections.IntHashMap;

public class GlobTypeIndexResolverImpl implements GlobTypeIndexResolver {
    private final IntHashMap<GlobType> types;

    public GlobTypeIndexResolverImpl(IntHashMap<GlobType> types) {
        this.types = types;
    }

    public GlobType fromIndex(int index) {
        return types.get(index);
    }
}
