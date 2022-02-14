package org.globsframework.serialisation.glob.type;

import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.field.reader.UnknownFieldReader;

public class GlobTypeFieldReaders {
    private final FieldReader[] fieldReaders;

    public GlobTypeFieldReaders(FieldReader[] fieldReaders) {
        this.fieldReaders = fieldReaders;
    }

    public FieldReader get(int fieldNumber) {
        return fieldNumber < fieldReaders.length ? fieldReaders[fieldNumber] : UnknownFieldReader.INSTANCE;
    }
}
