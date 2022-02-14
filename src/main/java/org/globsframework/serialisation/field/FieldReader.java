package org.globsframework.serialisation.field;

import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.stream.CodedInputStream;

import java.io.IOException;

public interface FieldReader {
    void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) throws IOException;

    int getFieldNumber();
}
