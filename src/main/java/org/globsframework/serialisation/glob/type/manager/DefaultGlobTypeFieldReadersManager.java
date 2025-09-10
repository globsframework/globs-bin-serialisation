package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultGlobTypeFieldReadersManager implements GlobTypeFieldReadersManager {
    private final Map<GlobType, GlobTypeFieldReaders> readersMap = new ConcurrentHashMap<>();
    private final GlobTypeFieldReadersFactory globTypeFieldReadersFactory;

    public DefaultGlobTypeFieldReadersManager(GlobTypeFieldReadersFactory globTypeFieldReadersFactory) {
        this.globTypeFieldReadersFactory = globTypeFieldReadersFactory;
    }

    public GlobTypeFieldReaders getOrCreate(GlobType globType) {
        return readersMap.computeIfAbsent(globType, globTypeFieldReadersFactory::create);
    }
}
