package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.GlobField;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.io.IOException;

public class GlobFieldWriter implements FieldWriter {
    private final BinWriter binWriter;
    private final int index;
    private final GlobField field;

    public GlobFieldWriter(BinWriter binWriter, int index, GlobField field) {
        this.binWriter = binWriter;
        this.index = index;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) throws IOException {
        if (!data.isSet(field)) {
            return;
        }
        Glob glob = data.get(field);
        if (glob == null) {
            codedOutputStream.writeNull(index);
        } else {
            codedOutputStream.writeGlob(index);
            binWriter.write(glob);
        }
    }

    public int getFieldNumber() {
        return index;
    }
}
