package org.globsframework.serialisation.field;

import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.stream.CodedInputStream;

import java.io.IOException;

public interface FieldReader {
    void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) throws IOException;

    int getFieldNumber();

    default void defaultReadCase(String fieldName, int tagWireType) {
        throw new RuntimeException("For " + fieldName + " unexpected type " + tagWireType);
    }
}
