package org.globsframework.serialisation.field;

import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.stream.CodedOutputStream;

public interface FieldWriter {
    void write(CodedOutputStream codedOutputStream, Glob data);

    int getFieldNumber();
}
