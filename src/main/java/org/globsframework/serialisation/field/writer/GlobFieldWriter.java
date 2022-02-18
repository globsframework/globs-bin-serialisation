package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.GlobField;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.io.IOException;

public class GlobFieldWriter implements FieldWriter {
    private final BinWriter binWriter;
    private final int fieldNumber;
    private final GlobField field;

    public GlobFieldWriter(BinWriter binWriter, int fieldNumber, GlobField field) {
        this.binWriter = binWriter;
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        if (!data.isSet(field)) {
            return;
        }
        Glob glob = data.get(field);
        if (glob == null) {
            codedOutputStream.writeNull(fieldNumber);
        } else {
            codedOutputStream.writeGlob(fieldNumber);
            binWriter.write(glob);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
