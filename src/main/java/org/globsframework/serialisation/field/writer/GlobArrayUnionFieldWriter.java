package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.GlobArrayUnionField;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.io.IOException;

public class GlobArrayUnionFieldWriter implements FieldWriter {
    private final BinWriter binWriter;
    private final int fieldNumber;
    private final GlobArrayUnionField field;

    public GlobArrayUnionFieldWriter(BinWriter binWriter, int fieldNumber, GlobArrayUnionField field) {
        this.binWriter = binWriter;
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        if (!data.isSet(field)) {
            return;
        }
        Glob[] globs = data.get(field);
        if (globs == null) {
            codedOutputStream.writeNull(fieldNumber);
        } else {
            codedOutputStream.writeGlobArrayUnion(fieldNumber, globs.length);
            for (Glob glob : globs) {
                binWriter.write(glob);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
