package org.globsframework.serialisation.field.writer;

import org.globsframework.core.metamodel.fields.*;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;

public class FieldWriterVisitorCreator extends FieldVisitorWithContext.AbstractFieldVisitor<Integer> {
    private final FieldWriter[] fieldWriters;
    private final GlobTypeFieldWritersFactory fieldWritersFactory;

    public FieldWriterVisitorCreator(FieldWriter[] fieldWriters,
                                     GlobTypeFieldWritersFactory fieldWritersFactory) {
        this.fieldWriters = fieldWriters;
        this.fieldWritersFactory = fieldWritersFactory;
    }

    public void visitBoolean(BooleanField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new BooleanFieldWriter(fieldNumber, field);
    }

    public void visitBooleanArray(BooleanArrayField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new BooleanArrayFieldWriter(fieldNumber, field);
    }

    public void visitInteger(IntegerField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new IntegerFieldWriter(fieldNumber, field);
    }

    public void visitIntegerArray(IntegerArrayField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new IntegerArrayFieldWriter(fieldNumber, field);
    }

    public void visitLong(LongField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new LongFieldWriter(fieldNumber, field);
    }

    public void visitLongArray(LongArrayField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new LongArrayFieldWriter(fieldNumber, field);
    }

    public void visitDouble(DoubleField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new DoubleFieldWriter(fieldNumber, field);
    }

    public void visitDoubleArray(DoubleArrayField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new DoubleArrayFieldWriter(fieldNumber, field);
    }

    public void visitBigDecimal(BigDecimalField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new BigDecimalFieldWriter(fieldNumber, field);
    }

    public void visitBigDecimalArray(BigDecimalArrayField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new BigDecimalArrayFieldWriter(fieldNumber, field);
    }

    public void visitString(StringField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new StringFieldWriter(fieldNumber, field);
    }

    public void visitStringArray(StringArrayField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new StringArrayFieldWriter(fieldNumber, field);
    }

    public void visitDate(DateField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new DateFieldWriter(fieldNumber, field);
    }

    public void visitDateTime(DateTimeField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new DateTimeFieldWriter(fieldNumber, field);
    }

    public void visitBytes(BytesField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new BytesFieldWriter(fieldNumber, field);
    }

    public void visitGlob(GlobField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new GlobFieldWriter(fieldNumber, field, fieldWritersFactory);
    }

    public void visitGlobArray(GlobArrayField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new GlobArrayFieldWriter(fieldNumber, field, fieldWritersFactory);
    }

    public void visitUnionGlob(GlobUnionField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new GlobUnionFieldWriter(fieldNumber, field, fieldWritersFactory);
    }

    public void visitUnionGlobArray(GlobArrayUnionField field, Integer fieldNumber) {
        fieldWriters[fieldNumber] = new GlobArrayUnionFieldWriter(fieldNumber, field, fieldWritersFactory);
    }
}
