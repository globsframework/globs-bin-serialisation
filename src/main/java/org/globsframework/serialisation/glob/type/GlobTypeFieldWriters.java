package org.globsframework.serialisation.glob.type;

import org.globsframework.serialisation.field.FieldWriter;

public class GlobTypeFieldWriters {
    private final int index;
    private final FieldWriter[] fieldWriters;

    public GlobTypeFieldWriters(int index, FieldWriter[] fieldWriters) {
        this.index = index;
        this.fieldWriters = fieldWriters;
    }

    public FieldWriter[] getFieldWriters() {
        return fieldWriters;
    }

    public int getGlobIndex() {
        return index;
    }
}
