package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.GlobArrayField;
import org.globsframework.core.model.FieldValuesAccessor;
import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class GlobArrayFieldWriter implements FieldWriter {
    private final BinWriter binWriter;
    private final int fieldNumber;
    private final GlobArrayField field;

    public GlobArrayFieldWriter(BinWriter binWriter, int fieldNumber, GlobArrayField field) {
        this.binWriter = binWriter;
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data) {
        if (!data.isSet(field)) {
            return;
        }
        Glob[] globs = data.get(field);
        if (globs == null) {
            codedOutputStream.writeNull(fieldNumber);
        } else {
            codedOutputStream.writeGlobArray(fieldNumber, globs.length);
            for (Glob glob : globs) {
                binWriter.write(glob);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
