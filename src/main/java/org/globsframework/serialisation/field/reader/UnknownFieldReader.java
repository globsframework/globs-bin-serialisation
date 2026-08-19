package org.globsframework.serialisation.field.reader;

import org.globsframework.core.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

public final class UnknownFieldReader implements FieldReader {
    public static final UnknownFieldReader INSTANCE = new UnknownFieldReader();

    private UnknownFieldReader() {
        // empty constructor
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        inputStream.skipFieldFromWireType(tagWireType);
    }

    /** The same read, driven by a ToGlobCaller : the CallAt has just read the tag. */
    public void call(MutableGlob data, CodedInputStream inputStream, Void ignored, Void alsoIgnored) {
        read(data, inputStream.lastTag(), inputStream.lastWireType(), inputStream);
    }

    public int getFieldNumber() {
        return 0;
    }
}
