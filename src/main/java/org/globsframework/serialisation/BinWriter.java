package org.globsframework.serialisation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.Glob;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public interface BinWriter {

    void write(@Nullable Glob glob);

    void write(@Nullable Collection<@Nullable Glob> glob);

    GlobWriter getWriter(GlobType type);

    interface GlobWriter {
        void write(@Nullable  Glob glob);
        void write(@Nullable Collection<@Nullable Glob> globs);
    }
}
