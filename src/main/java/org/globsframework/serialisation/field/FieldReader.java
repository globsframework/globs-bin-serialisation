package org.globsframework.serialisation.field;

import org.globsframework.core.model.MutableGlob;
import org.globsframework.serialisation.stream.CodedInputStream;

public interface FieldReader {
    void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream);

    int getFieldNumber();

    default void defaultReadCase(String fieldName, int tagWireType) {
        throw new RuntimeException("For " + fieldName + " unexpected type " + tagWireType);
    }
}
