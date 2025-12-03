package org.globsframework.serialisation.glob.type;

import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class GlobTypeFieldWriters {
    private final FieldWriter[] fieldWriters;

    public GlobTypeFieldWriters(FieldWriter[] fieldWriters) {
        this.fieldWriters = fieldWriters;
    }

    public void write(CodedOutputStream codedOutputStream, Glob glob) {
        if (glob == null) {
            codedOutputStream.writeNull();
        } else {
            codedOutputStream.writeStartGlob();
            for (FieldWriter fieldWriter : fieldWriters) {
                fieldWriter.write(codedOutputStream, glob);
            }
            codedOutputStream.writeEndGlob();
        }
    }
}
