package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;

import java.util.Map;

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
