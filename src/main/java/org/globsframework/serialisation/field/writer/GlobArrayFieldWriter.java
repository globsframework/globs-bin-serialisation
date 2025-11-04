package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.GlobArrayField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetGlobArrayAccessor;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class GlobArrayFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetGlobArrayAccessor getAccessor;

    public GlobArrayFieldWriter(int fieldNumber, GlobArrayField field) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
    }

    public void write(CodedOutputStream codedOutputStream, Glob data, BinWriter binWriter) {
        Glob[] globs = getAccessor.get(data);
        if (globs == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
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
