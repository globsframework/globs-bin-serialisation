package org.globsframework.serialisation.glob.type.factory;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.field.reader.FieldReaderVisitorCreator;
import org.globsframework.serialisation.field.reader.UnknownFieldReader;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.model.FieldNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldWritersFactory.getGreatestID;

public class DefaultGlobTypeFieldReadersFactory implements GlobTypeFieldReadersFactory {
    private static final Logger log = LoggerFactory.getLogger(DefaultGlobTypeFieldReadersFactory.class);
    private final Map<GlobType, GlobTypeFieldReaders> containers;

    public DefaultGlobTypeFieldReadersFactory(Map<GlobType, GlobTypeFieldReaders> containers) {
        this.containers = containers;
    }

    public synchronized GlobTypeFieldReaders create(GlobType globType) {
        final GlobTypeFieldReaders globTypeFieldReaders = containers.get(globType);
        if (globTypeFieldReaders != null) {
            return globTypeFieldReaders;
        }
        log.info("Creating readers for {}", globType.getName());

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

}
