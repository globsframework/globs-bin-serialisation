package org.globsframework.serialisation.field.writer;

import org.globsframework.metamodel.fields.*;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;

import java.util.List;

public class FieldWriterVisitorCreator extends FieldVisitor.AbstractFieldVisitor {
    private final BinWriter binWriter;
    private final int fieldNumber;
    private final List<FieldWriter> fieldWriters;

    public FieldWriterVisitorCreator(BinWriter binWriter, int fieldNumber, List<FieldWriter> fieldWriters) {
        this.binWriter = binWriter;
        this.fieldNumber = fieldNumber;
        this.fieldWriters = fieldWriters;
    }

    public void visitInteger(IntegerField field) {
        fieldWriters.add(new IntFieldWriter(fieldNumber, field));
    }

    public void visitLong(LongField field) {
        fieldWriters.add(new LongFieldWriter(fieldNumber, field));
    }

    public void visitDouble(DoubleField field) {
        fieldWriters.add(new DoubleFieldWriter(fieldNumber, field));
    }

    public void visitGlob(GlobField field) {
        fieldWriters.add(new GlobFieldWriter(binWriter, fieldNumber, field));
    }
}
