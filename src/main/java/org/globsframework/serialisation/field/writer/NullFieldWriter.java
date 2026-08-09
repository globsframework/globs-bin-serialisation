package org.globsframework.serialisation.field.writer;

import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public final class NullFieldWriter implements FieldWriter {
    public static final NullFieldWriter INSTANCE = new NullFieldWriter();

    private NullFieldWriter() {
        // empty constructor
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        // do nothing
    }

    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        // do nothing : a field with no field number is not written, whatever it holds
    }

    public int getFieldNumber() {
        return 0;
    }
}
