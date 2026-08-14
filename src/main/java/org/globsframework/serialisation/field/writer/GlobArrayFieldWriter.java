package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.GlobArrayField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetGlobArrayAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;
import org.globsframework.serialisation.stream.CodedOutputStream;

public record GlobArrayFieldWriter(int fieldNumber, GlobGetGlobArrayAccessor getAccessor, GlobTypeFieldWriters globTypeFieldWriters) implements FieldWriter {

    public GlobArrayFieldWriter(int fieldNumber, GlobArrayField<?> field, GlobTypeFieldWritersFactory fieldWritersFactory) {
        this(fieldNumber,
                (GlobGetGlobArrayAccessor) field.getGlobType().getGetAccessor(field),
                fieldWritersFactory.create(field.getTargetType()));
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        Glob[] globs = getAccessor.get(data);
        if (globs == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            writeValue(codedOutputStream, globs);
        }
    }

    /** The same thing driven by a GeneratedFunctionCaller : isSet / isNull / the value come from it. */
    public void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream, Void ignored) {
        if (isNull) {
            if (isSet) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            writeValue(codedOutputStream, (Glob[]) value);
        }
    }

    private void writeValue(CodedOutputStream codedOutputStream, Glob[] globs) {
        codedOutputStream.writeGlobArray(fieldNumber, globs.length);
        for (Glob glob : globs) {
            globTypeFieldWriters.write(codedOutputStream, glob);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
