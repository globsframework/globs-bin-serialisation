package org.globsframework.serialisation.glob.type;

import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.stream.CodedOutputStream;

public interface GlobTypeFieldWriters {
    void write(CodedOutputStream codedOutputStream, Glob glob);
}
