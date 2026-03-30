package org.globsframework.serialisation.glob.type.factory;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.field.writer.FieldWriterVisitorCreator;
import org.globsframework.serialisation.field.writer.NullFieldWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.model.FieldNumber;

import java.util.Arrays;
import java.util.Map;

public class DefaultGlobTypeFieldWritersFactory implements GlobTypeFieldWritersFactory {
    private final Map<GlobType, GlobTypeFieldWriters> containers;

    public DefaultGlobTypeFieldWritersFactory(Map<GlobType, GlobTypeFieldWriters> containers) {
        this.containers = containers;
    }

    public synchronized GlobTypeFieldWriters create(GlobType type) {
        if (containers.containsKey(type)) {
            return containers.get(type);
        }

        Field[] fields = type.getFields();
        FieldWriter[] realFieldWriters = new FieldWriter[fields.length];

        final GlobTypeFieldWriters fieldWriters = new GlobTypeFieldWriters(realFieldWriters);
        containers.put(type, fieldWriters);

        Arrays.fill(realFieldWriters, NullFieldWriter.INSTANCE);

        final FieldWriterVisitorCreator fieldWriterVisitorCreator =
                new FieldWriterVisitorCreator(realFieldWriters, this);
        for (Field field : fields) {
            int ind = field.findOptAnnotation(FieldNumber.KEY)
                    .map(FieldNumber.fieldNumber)
                    .orElse(-1);
            if (ind != -1) {
                field.safeAccept(fieldWriterVisitorCreator, ind);
            }
        }

        return fieldWriters;
    }

}
