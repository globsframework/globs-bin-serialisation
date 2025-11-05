package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.*;
import org.globsframework.serialisation.field.FieldWriter;

import java.util.List;

public class FieldWriterVisitorCreator extends FieldVisitor.AbstractFieldVisitor {
    private final int fieldNumber;
    private final List<FieldWriter> fieldWriters;

    public FieldWriterVisitorCreator(int fieldNumber, List<FieldWriter> fieldWriters) {
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

    public void visitDoubleArray(DoubleArrayField field) {
        fieldWriters.add(new DoubleArrayFieldWriter(fieldNumber, field));
    }

    public void visitBigDecimal(BigDecimalField field) {
        fieldWriters.add(new BigDecimalFieldWriter(fieldNumber, field));
    }

    public void visitBigDecimalArray(BigDecimalArrayField field) {
        fieldWriters.add(new BigDecimalArrayFieldWriter(fieldNumber, field));
    }

    public void visitString(StringField field) {
        fieldWriters.add(new StringFieldWriter(fieldNumber, field));
    }

    public void visitStringArray(StringArrayField field) {
        fieldWriters.add(new StringArrayFieldWriter(fieldNumber, field));
    }

    public void visitDate(DateField field) {
        fieldWriters.add(new DateFieldWriter(fieldNumber, field));
    }

    public void visitDateTime(DateTimeField field) {
        fieldWriters.add(new DateTimeFieldWriter(fieldNumber, field));
    }

    public void visitBytes(BytesField field) {
        fieldWriters.add(new BytesFieldWriter(fieldNumber, field));
    }

    public void visitGlob(GlobField field) {
        fieldWriters.add(new GlobFieldWriter(fieldNumber, field));
    }

    public void visitGlobArray(GlobArrayField field) {
        fieldWriters.add(new GlobArrayFieldWriter(fieldNumber, field));
    }

    public void visitUnionGlob(GlobUnionField field) {
        fieldWriters.add(new GlobUnionFieldWriter(fieldNumber, field));
    }

    public void visitUnionGlobArray(GlobArrayUnionField field) {
        fieldWriters.add(new GlobArrayUnionFieldWriter(fieldNumber, field));
    }
}
