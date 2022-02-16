package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultGlobTypeFieldReadersManager implements GlobTypeFieldReadersManager {
    private final Map<String, GlobTypeFieldReaders> readersMap = new ConcurrentHashMap<>();

    public GlobTypeFieldReaders getOrCreate(GlobType globType, GlobTypeFieldReadersFactory factory) {
       return readersMap.computeIfAbsent(globType.getName(), s -> factory.create(globType));
    }

}
