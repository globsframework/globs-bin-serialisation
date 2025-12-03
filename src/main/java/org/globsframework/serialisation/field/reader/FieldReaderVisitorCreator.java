package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.fields.*;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;

public class FieldReaderVisitorCreator extends FieldVisitorWithContext.AbstractFieldVisitor<Integer> {
    private final FieldReader[] fieldReaders;
    private final GlobTypeFieldReadersFactory globTypeFieldReadersFactory;

    public FieldReaderVisitorCreator(FieldReader[] fieldReaders, GlobTypeFieldReadersFactory globTypeFieldReadersFactory) {
        this.fieldReaders = fieldReaders;
        this.globTypeFieldReadersFactory = globTypeFieldReadersFactory;
    }

    public void visitBoolean(BooleanField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new BooleanFieldReader(fieldNumber, field);
    }

    public void visitBooleanArray(BooleanArrayField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new BooleanArrayFieldReader(fieldNumber, field);
    }

    public void visitInteger(IntegerField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new IntegerFieldReader(fieldNumber, field);
    }

    public void visitIntegerArray(IntegerArrayField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new IntegerArrayFieldReader(fieldNumber, field);
    }

    public void visitLong(LongField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new LongFieldReader(fieldNumber, field);
    }

    public void visitLongArray(LongArrayField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new LongArrayFieldReader(fieldNumber, field);
    }

    public void visitDouble(DoubleField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new DoubleFieldReader(fieldNumber, field);
    }

    public void visitDoubleArray(DoubleArrayField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new DoubleArrayFieldReader(fieldNumber, field);
    }

    public void visitBigDecimal(BigDecimalField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new BigDecimalFieldReader(fieldNumber, field);
    }

    public void visitBigDecimalArray(BigDecimalArrayField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new BigDecimalArrayFieldReader(fieldNumber, field);
    }

    public void visitString(StringField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new StringFieldReader(fieldNumber, field);
    }

    public void visitStringArray(StringArrayField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new StringArrayFieldReader(fieldNumber, field);
    }

    public void visitDate(DateField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new DateFieldReader(fieldNumber, field);
    }

    public void visitDateTime(DateTimeField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new DateTimeFieldReader(fieldNumber, field);
    }

    public void visitBytes(BytesField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new BytesFieldReader(fieldNumber, field);
    }

    public void visitGlob(GlobField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new GlobFieldReader(fieldNumber, field, globTypeFieldReadersFactory);
    }

    public void visitGlobArray(GlobArrayField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new GlobArrayFieldReader(fieldNumber, field, globTypeFieldReadersFactory);
    }

    public void visitUnionGlob(GlobUnionField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new GlobUnionFieldReader(fieldNumber, field, globTypeFieldReadersFactory);
    }

    public void visitUnionGlobArray(GlobArrayUnionField field, Integer fieldNumber) {
        fieldReaders[fieldNumber] = new GlobArrayUnionFieldReader(fieldNumber, field, globTypeFieldReadersFactory);
    }
}
