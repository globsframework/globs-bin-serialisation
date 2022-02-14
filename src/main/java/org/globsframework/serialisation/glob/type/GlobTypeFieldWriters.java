package org.globsframework.serialisation.glob.type;

import org.globsframework.serialisation.field.FieldWriter;

public class GlobTypeFieldWriters {
    private final FieldWriter[] fieldWriters;

    public GlobTypeFieldWriters(FieldWriter[] fieldWriters) {
        this.fieldWriters = fieldWriters;
    }

    public FieldWriter[] getFieldWriters() {
        return fieldWriters;
    }
}
