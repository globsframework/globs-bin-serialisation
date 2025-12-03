package org.globsframework.serialisation.glob.type.factory;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.field.writer.FieldWriterVisitorCreator;
import org.globsframework.serialisation.field.writer.NullFieldWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.model.FieldNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DefaultGlobTypeFieldWritersFactory implements GlobTypeFieldWritersFactory {
    private static final Logger log = LoggerFactory.getLogger(DefaultGlobTypeFieldWritersFactory.class);
    private final Map<GlobType, GlobTypeFieldWriters> containers;

    public DefaultGlobTypeFieldWritersFactory(Map<GlobType, GlobTypeFieldWriters> containers) {
        this.containers = containers;
    }

    public synchronized GlobTypeFieldWriters create(GlobType type) {
        if (containers.containsKey(type)) {
            return containers.get(type);
        }

        log.info("Creating writers for {}", type.getName());

        Field[] fields = type.getFields();
        final int maxLen = getGreatestID(fields);
        FieldWriter[] realFieldWriters = new FieldWriter[maxLen + 1];

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

    public static int getGreatestID(Field[] fields) {
        int maxLen = 0;
        for (Field field : fields) {
            maxLen = Math.max(maxLen, field.findOptAnnotation(FieldNumber.KEY)
                    .map(FieldNumber.fieldNumber)
                    .orElse(-1));
        }
        return maxLen;
    }

}
