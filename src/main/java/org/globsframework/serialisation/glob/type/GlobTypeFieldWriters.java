package org.globsframework.serialisation.glob.type;

import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.stream.CodedOutputStream;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GlobTypeFieldWriters {
    void write(CodedOutputStream codedOutputStream, Glob glob);
}
