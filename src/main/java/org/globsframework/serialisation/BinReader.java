package org.globsframework.serialisation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.Glob;
import org.jspecify.annotations.Nullable;

public interface BinReader {
    @Nullable Glob read(GlobType type);

    @Nullable Glob[] readArray(GlobType type);

    GlobReader getReader(GlobType type);

    interface GlobReader {
        @Nullable Glob read();
        @Nullable Glob[] readArray();
    }
}
