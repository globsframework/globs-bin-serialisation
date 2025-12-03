package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;

import java.util.Map;

public class DefaultGlobTypeFieldWritersManager implements GlobTypeFieldWritersManager {
    private final Map<GlobType, GlobTypeFieldWriters> preInit;
    private final GlobTypeFieldWritersFactory globTypeFieldWritersFactory;

    public DefaultGlobTypeFieldWritersManager(Map<GlobType, GlobTypeFieldWriters> preInit, GlobTypeFieldWritersFactory globTypeFieldWritersFactory) {
        this.preInit = preInit;
        this.globTypeFieldWritersFactory = globTypeFieldWritersFactory;
    }

    public GlobTypeFieldWriters getOrCreate(GlobType globType) {
        final GlobTypeFieldWriters globTypeFieldWriters = preInit.get(globType);
        if (globTypeFieldWriters != null) {
            return globTypeFieldWriters;
        }
        return globTypeFieldWritersFactory.create(globType);
    }
}
