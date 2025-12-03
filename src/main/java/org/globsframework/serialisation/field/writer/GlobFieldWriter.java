package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.GlobField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetGlobAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class GlobFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobGetGlobAccessor getAccessor;
    private final GlobTypeFieldWriters globTypeFieldWriters;

    public GlobFieldWriter(int fieldNumber, GlobField field, GlobTypeFieldWritersFactory fieldWritersFactory) {
        this.fieldNumber = fieldNumber;
        getAccessor = field.getGlobType().getGetAccessor(field);
        globTypeFieldWriters = fieldWritersFactory.create(field.getTargetType());
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        Glob glob = getAccessor.get(data);
        if (glob == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            codedOutputStream.writeGlob(fieldNumber);
            globTypeFieldWriters.write(codedOutputStream, glob);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
