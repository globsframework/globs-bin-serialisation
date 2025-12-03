package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.GlobUnionField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.globaccessor.get.GlobGetGlobAccessor;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.util.Map;

import static org.globsframework.serialisation.field.writer.GlobArrayUnionFieldWriter.initTypesByIndex;

public class GlobUnionFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobUnionField field;
    private final Map<GlobType, IndiceWithWriter> types;
    private final GlobGetGlobAccessor getAccessor;

    public record IndiceWithWriter(int indice, GlobTypeFieldWriters fieldWriters) {
    }

    public GlobUnionFieldWriter(int fieldNumber, GlobUnionField field, GlobTypeFieldWritersFactory fieldWritersFactory) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        types = initTypesByIndex(field, field.getTargetTypes(), fieldWritersFactory);
        getAccessor = field.getGlobType().getGetAccessor(field);
    }

    public void write(CodedOutputStream codedOutputStream, Glob data) {
        Glob glob = getAccessor.get(data);
        if (glob == null) {
            if (getAccessor.isSet(data)) {
                codedOutputStream.writeNull(fieldNumber);
            }
        } else {
            final IndiceWithWriter index = types.get(glob.getType());
            if (index == null) {
                throw new RuntimeException("Unsupported glob type " + glob.getType() + " in " + field.getFullName());
            }
            codedOutputStream.writeGlobUnion(fieldNumber);
            codedOutputStream.writeInt(index.indice);
            index.fieldWriters.write(codedOutputStream, glob);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
