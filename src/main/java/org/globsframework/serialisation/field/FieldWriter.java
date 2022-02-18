package org.globsframework.serialisation.field;

import org.globsframework.model.Glob;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.io.IOException;

public interface FieldWriter {
    void write(CodedOutputStream codedOutputStream, Glob data);

    int getFieldNumber();
}
