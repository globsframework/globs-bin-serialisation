package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.*;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;

public final class FieldWriterVisitorCreator extends FieldVisitorWithContext.AbstractFieldVisitor<Integer> {
    private final FieldWriter[] fieldWriters;
    private final GlobTypeFieldWritersFactory fieldWritersFactory;

    public FieldWriterVisitorCreator(FieldWriter[] fieldWriters,
                                     GlobTypeFieldWritersFactory fieldWritersFactory) {
        this.fieldWriters = fieldWriters;
        this.fieldWritersFactory = fieldWritersFactory;
    }

    public void visitBoolean(BooleanField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new BooleanFieldWriter(fieldNumber, field);
    }

    public void visitBooleanArray(BooleanArrayField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new BooleanArrayFieldWriter(fieldNumber, field);
    }

    public void visitInteger(IntegerField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new IntegerFieldWriter(fieldNumber, field);
    }

    public void visitIntegerArray(IntegerArrayField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new IntegerArrayFieldWriter(fieldNumber, field);
    }

    public void visitLong(LongField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new LongFieldWriter(fieldNumber, field);
    }

    public void visitLongArray(LongArrayField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new LongArrayFieldWriter(fieldNumber, field);
    }

    public void visitDouble(DoubleField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new DoubleFieldWriter(fieldNumber, field);
    }

    public void visitDoubleArray(DoubleArrayField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new DoubleArrayFieldWriter(fieldNumber, field);
    }

    public void visitBigDecimal(BigDecimalField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new BigDecimalFieldWriter(fieldNumber, field);
    }

    public void visitBigDecimalArray(BigDecimalArrayField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new BigDecimalArrayFieldWriter(fieldNumber, field);
    }

    public void visitString(StringField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new StringFieldWriter(fieldNumber, field);
    }

    public void visitStringArray(StringArrayField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new StringArrayFieldWriter(fieldNumber, field);
    }

    public void visitDate(DateField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new DateFieldWriter(fieldNumber, field);
    }

    public void visitDateTime(DateTimeField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new DateTimeFieldWriter(fieldNumber, field);
    }

    public void visitBytes(BytesField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new BytesFieldWriter(fieldNumber, field);
    }

    public void visitGlob(GlobField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new GlobFieldWriter(fieldNumber, field, fieldWritersFactory);
    }

    public void visitGlobArray(GlobArrayField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new GlobArrayFieldWriter(fieldNumber, field, fieldWritersFactory);
    }

    public void visitUnionGlob(GlobUnionField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new GlobUnionFieldWriter(fieldNumber, field, fieldWritersFactory);
    }

    public void visitUnionGlobArray(GlobArrayUnionField field, Integer fieldNumber) {
        fieldWriters[field.getIndex()] = new GlobArrayUnionFieldWriter(fieldNumber, field, fieldWritersFactory);
    }
}
