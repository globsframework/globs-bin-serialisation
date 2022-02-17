package org.globsframework.serialisation.field.reader;

import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public class UnknownFieldReader implements FieldReader {
    public static final UnknownFieldReader INSTANCE = new UnknownFieldReader();

    private UnknownFieldReader() {
        // empty constructor
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        inputStream.skipField(tag);
    }

    public int getFieldNumber() {
        return 0;
    }
}
