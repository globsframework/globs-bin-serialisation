package org.globsframework.serialisation.field.writer;

import org.globsframework.core.model.FieldValuesAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class NullFieldWriter implements FieldWriter {
    public static final NullFieldWriter INSTANCE = new NullFieldWriter();

    private NullFieldWriter() {
        // empty constructor
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data) {
        // do nothing
    }

    public int getFieldNumber() {
        return 0;
    }
}
