package org.globsframework.serialisation.glob.type;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.utils.FieldCheck;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record ArraysGlobTypeFieldWriters(GlobType type, FieldWriter[] fieldWriters) implements GlobTypeFieldWriters {

    @Override
    public void write(CodedOutputStream codedOutputStream, Glob glob) {
        FieldCheck.check(type, glob);
        codedOutputStream.writeStartGlob();
        for (FieldWriter fieldWriter : fieldWriters) {
            fieldWriter.write(codedOutputStream, glob);
        }
        codedOutputStream.writeEndGlob();
    }
}
