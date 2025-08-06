package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.GlobUnionField;
import org.globsframework.core.model.FieldValuesAccessor;
import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.util.Map;

import static org.globsframework.serialisation.field.writer.GlobArrayUnionFieldWriter.initTypesByIndex;

public class GlobUnionFieldWriter implements FieldWriter {
    private final int fieldNumber;
    private final GlobUnionField field;
    private final Map<GlobType, Integer> types;

    public GlobUnionFieldWriter(int fieldNumber, GlobUnionField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        types = initTypesByIndex(field, field.getTargetTypes());
    }

    public void write(CodedOutputStream codedOutputStream, FieldValuesAccessor data, BinWriter binWriter) {
        if (!data.isSet(field)) {
            return;
        }
        Glob glob = data.get(field);
        if (glob == null) {
            codedOutputStream.writeNull(fieldNumber);
        } else {
            final Integer index = types.get(glob.getType());
            if (index == null) {
                throw new RuntimeException("Unsupported glob type " + glob.getType() + " in " + field.getFullName());
            }
            codedOutputStream.writeGlobUnion(fieldNumber);
            codedOutputStream.writeInt(index);
            binWriter.write(glob);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
