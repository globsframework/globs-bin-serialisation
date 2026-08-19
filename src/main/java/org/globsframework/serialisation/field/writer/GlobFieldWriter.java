package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.GlobField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetGlobAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;
import org.globsframework.serialisation.stream.CodedOutputStream;

public record GlobFieldWriter(int fieldNumber, GlobGetGlobAccessor getAccessor, GlobTypeFieldWriters globTypeFieldWriters) implements FieldWriter {

    public GlobFieldWriter(int fieldNumber, GlobField<?> field, GlobTypeFieldWritersFactory fieldWritersFactory) {
        this(fieldNumber,
                (GlobGetGlobAccessor) field.getGlobType().getGetAccessor(field),
                fieldWritersFactory.create(field.getTargetType()));
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        Glob glob = getAccessor.get(data);
        if (glob == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            writeValue(codedOutputStream, glob);
        }
    }

    /** The same thing driven by a FromGlobCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            writeValue(codedOutputStream, (Glob) value);
        }
    }

    private void writeValue(CodedOutputStream codedOutputStream, Glob glob) {
        codedOutputStream.writeGlob(fieldNumber);
        globTypeFieldWriters.write(codedOutputStream, glob);
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
