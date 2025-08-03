package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.GlobArrayUnionField;
import org.globsframework.core.model.FieldValuesAccessor;
import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class GlobArrayUnionFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobArrayUnionField field;

    public GlobArrayUnionFieldWriter(int fieldNumber, GlobArrayUnionField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data, BinWriter binWriter) {
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
