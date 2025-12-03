package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.GlobArrayField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetGlobArrayAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class GlobArrayFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetGlobArrayAccessor getAccessor;
    private final GlobTypeFieldWriters globTypeFieldWriters;

    public GlobArrayFieldWriter(int fieldNumber, GlobArrayField field, GlobTypeFieldWritersFactory fieldWritersFactory) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
        globTypeFieldWriters = fieldWritersFactory.create(field.getTargetType());
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        Glob[] globs = getAccessor.get(data);
        if (globs == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeGlobArray(fieldNumber, globs.length);
            for (Glob glob : globs) {
                globTypeFieldWriters.write(codedOutputStream, glob);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
