package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.field.reader.FieldReaderVisitorCreator;
import org.globsframework.serialisation.field.reader.UnknownFieldReader;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;
import org.globsframework.serialisation.model.FieldNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultGlobTypeFieldReadersManager implements GlobTypeFieldReadersManager {
    private final Map<String, GlobTypeFieldReaders> readersMap = new ConcurrentHashMap<>();
    private final GlobTypeFieldReadersFactory globTypeFieldReadersFactory;

    public DefaultGlobTypeFieldReadersManager(GlobTypeFieldReadersFactory globTypeFieldReadersFactory) {
        this.globTypeFieldReadersFactory = globTypeFieldReadersFactory;
    }

    public GlobTypeFieldReaders getOrCreate(GlobType globType) {
        return readersMap.computeIfAbsent(globType.getName(), s -> globTypeFieldReadersFactory.create(globType));
    }

}
