package org.globsframework.serialisation.glob.type.factory;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.field.reader.FieldReaderVisitorCreator;
import org.globsframework.serialisation.field.reader.UnknownFieldReader;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.model.FieldNumber;

import java.util.Arrays;
import java.util.Map;

public class DefaultGlobTypeFieldReadersFactory implements GlobTypeFieldReadersFactory {
    private final Map<GlobType, GlobTypeFieldReaders> containers;

    public DefaultGlobTypeFieldReadersFactory(Map<GlobType, GlobTypeFieldReaders> containers) {
        this.containers = containers;
    }

    public synchronized GlobTypeFieldReaders create(GlobType globType) {
        final GlobTypeFieldReaders globTypeFieldReaders = containers.get(globType);
        if (globTypeFieldReaders != null) {
            return globTypeFieldReaders;
        }

        Field[] fields = globType.getFields();
        final int maxLen = getGreatestID(fields);
        FieldReader[] readers = new FieldReader[maxLen + 1];

        final GlobTypeFieldReaders v = new GlobTypeFieldReaders(readers);
        containers.put(globType, v);

        Arrays.fill(readers, UnknownFieldReader.INSTANCE);

        final FieldReaderVisitorCreator fieldReaderVisitorCreator = new FieldReaderVisitorCreator(readers, this);
        for (Field field : fields) {
            final Integer ind = field.findOptAnnotation(FieldNumber.KEY)
                    .map(FieldNumber.fieldNumber)
                    .orElse(-1);
            if (ind != -1) {
                field.safeAccept(fieldReaderVisitorCreator, ind);
            }
        }
        return v;
    }

    private static int getGreatestID(Field[] fields) {
        int maxLen = 0;
        for (Field field : fields) {
            maxLen = Math.max(maxLen, field.findOptAnnotation(FieldNumber.KEY)
                    .map(FieldNumber.fieldNumber)
                    .orElse(-1));
        }
        return maxLen;
    }

}
