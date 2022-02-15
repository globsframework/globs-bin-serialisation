package org.globsframework.serialisation.field.reader;

import org.globsframework.metamodel.fields.FieldVisitor;
import org.globsframework.metamodel.fields.GlobField;
import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.metamodel.fields.LongField;
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

    public void visitInteger(IntegerField field) {
        fieldReaders.add(new IntegerFieldReader(fieldNumber, field));
    }

    public void visitLong(LongField field) {
        fieldReaders.add(new LongFieldReader(fieldNumber, field));
    }

    public void visitGlob(GlobField field) {
        fieldReaders.add(new GlobFieldReader(binReader, fieldNumber, field));
    }
}
