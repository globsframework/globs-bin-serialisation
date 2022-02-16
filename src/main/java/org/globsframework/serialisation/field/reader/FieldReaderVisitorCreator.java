package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.fields.*;
import org.globsframework.serialisation.BinReader;
import org.globsframework.serialisation.field.FieldReader;

import java.util.List;

public class FieldReaderVisitorCreator extends FieldVisitor.AbstractFieldVisitor {
    private final BinReader binReader;
    private final int fieldNumber;
    private final List<FieldReader> fieldReaders;

    public FieldReaderVisitorCreator(BinReader binReader, int fieldNumber, List<FieldReader> fieldReaders) {
        this.binReader = binReader;
        this.fieldNumber = fieldNumber;
        this.fieldReaders = fieldReaders;
    }

    public void visitBoolean(BooleanField field) {
        fieldReaders.add(new BooleanFieldReader(fieldNumber, field));
    }

    public void visitInteger(IntegerField field) {
        fieldReaders.add(new IntegerFieldReader(fieldNumber, field));
    }

    public void visitLong(LongField field) {
        fieldReaders.add(new LongFieldReader(fieldNumber, field));
    }

    public void visitDouble(DoubleField field) {
        fieldReaders.add(new DoubleFieldReader(fieldNumber, field));
    }

    public void visitBigDecimal(BigDecimalField field) {
        fieldReaders.add(new BigDecimalFieldReader(fieldNumber, field));
    }

    public void visitString(StringField field) {
        fieldReaders.add(new StringFieldReader(fieldNumber, field));
    }

    public void visitDate(DateField field) {
        fieldReaders.add(new DateFieldReader(fieldNumber, field));
    }

    public void visitDateTime(DateTimeField field) {
        fieldReaders.add(new DateTimeFieldReader(fieldNumber, field));
    }

    public void visitGlob(GlobField field) {
        fieldReaders.add(new GlobFieldReader(binReader, fieldNumber, field));
    }
}
