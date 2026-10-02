package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;

import java.util.Map;

public class PropertiesBasedGlobTypeFieldReadersManager implements GlobTypeFieldReadersManager, GlobType.Build<GlobTypeFieldReaders> {
    private static final GlobType.Property<GlobTypeFieldReaders> property = new GlobType.Property<>();
    private final GlobTypeFieldReadersFactory globTypeFieldReadersFactory;

    public PropertiesBasedGlobTypeFieldReadersManager(Map<GlobType, GlobTypeFieldReaders> preInitMap,
                                                      GlobTypeFieldReadersFactory globTypeFieldReadersFactory) {
        this.globTypeFieldReadersFactory = globTypeFieldReadersFactory;
        for (Map.Entry<GlobType, GlobTypeFieldReaders> globTypeGlobTypeFieldReadersEntry : preInitMap.entrySet()) {
            globTypeGlobTypeFieldReadersEntry.getKey().init(property, globTypeGlobTypeFieldReadersEntry.getValue());
        }
    }

    public GlobTypeFieldReaders getOrCreate(GlobType globType) {
        return globType.get(property, this);
    }

    @Override
    public GlobTypeFieldReaders create(GlobType globType) {
        return globTypeFieldReadersFactory.create(globType);
    }
}
