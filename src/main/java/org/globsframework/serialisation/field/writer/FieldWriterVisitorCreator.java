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

    public void visitBoolean(BooleanField field) {
        fieldWriters.add(new BooleanFieldWriter(fieldNumber, field));
    }

    public void visitBooleanArray(BooleanArrayField field) {
        fieldWriters.add(new BooleanArrayFieldWriter(fieldNumber, field));
    }

    public void visitInteger(IntegerField field) {
        fieldWriters.add(new IntegerFieldWriter(fieldNumber, field));
    }

    public void visitIntegerArray(IntegerArrayField field) {
        fieldWriters.add(new IntegerArrayFieldWriter(fieldNumber, field));
    }

    public void visitLong(LongField field) {
        fieldWriters.add(new LongFieldWriter(fieldNumber, field));
    }

    public void visitLongArray(LongArrayField field) {
        fieldWriters.add(new LongArrayFieldWriter(fieldNumber, field));
    }

    public void visitDouble(DoubleField field) {
        fieldWriters.add(new DoubleFieldWriter(fieldNumber, field));
    }

    public void visitBigDecimal(BigDecimalField field) {
        fieldWriters.add(new BigDecimalFieldWriter(fieldNumber, field));
    }

    public void visitString(StringField field) {
        fieldWriters.add(new StringFieldWriter(fieldNumber, field));
    }

    public void visitDate(DateField field) {
        fieldWriters.add(new DateFieldWriter(fieldNumber, field));
    }

    public void visitDateTime(DateTimeField field) {
        fieldWriters.add(new DateTimeFieldWriter(fieldNumber, field));
    }

    public void visitBlob(BlobField field) {
        fieldWriters.add(new BlobFieldWriter(fieldNumber, field));
    }

    public void visitGlob(GlobField field) {
        fieldWriters.add(new GlobFieldWriter(binWriter, fieldNumber, field));
    }
}
