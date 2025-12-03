package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldReadersFactory;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultGlobTypeFieldReadersManager implements GlobTypeFieldReadersManager {
    private final Map<GlobType, GlobTypeFieldReaders> preInitMap;
    private final GlobTypeFieldReadersFactory globTypeFieldReadersFactory;

    public DefaultGlobTypeFieldReadersManager(Map<GlobType, GlobTypeFieldReaders> preInitMap,
                                              GlobTypeFieldReadersFactory globTypeFieldReadersFactory) {
        this.preInitMap = preInitMap;
        this.globTypeFieldReadersFactory = globTypeFieldReadersFactory;
    }

    public GlobTypeFieldReaders getOrCreate(GlobType globType) {
        final GlobTypeFieldReaders globTypeFieldReaders = preInitMap.get(globType);
        if (globTypeFieldReaders != null) {
            return globTypeFieldReaders;
        }
        return globTypeFieldReadersFactory.create(globType);
    }
}
