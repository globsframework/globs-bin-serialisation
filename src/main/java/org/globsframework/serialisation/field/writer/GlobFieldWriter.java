package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.GlobField;
import org.globsframework.core.model.FieldValuesAccessor;
import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class GlobFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobField field;

    public GlobFieldWriter(int fieldNumber, GlobField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data, BinWriter binWriter) {
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
