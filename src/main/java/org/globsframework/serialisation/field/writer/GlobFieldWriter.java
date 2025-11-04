package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.GlobField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetGlobAccessor;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class GlobFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetGlobAccessor getAccessor;

    public GlobFieldWriter(int fieldNumber, GlobField field) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
    }

    public void write(CodedOutputStream codedOutputStream, Glob data, BinWriter binWriter) {
        Glob glob = getAccessor.get(data);
        if (glob == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeGlob(fieldNumber);
            binWriter.write(glob);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
