package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;

import java.util.Map;

public class PropertiesBasedGlobTypeFieldWritersManager implements GlobTypeFieldWritersManager, GlobType.Build<GlobTypeFieldWriters> {
    private static final GlobType.Property<GlobTypeFieldWriters> property = new GlobType.Property<>();
    private final GlobTypeFieldWritersFactory globTypeFieldWritersFactory;

    public PropertiesBasedGlobTypeFieldWritersManager(Map<GlobType, GlobTypeFieldWriters> preInit,
                                                      GlobTypeFieldWritersFactory globTypeFieldWritersFactory) {
        this.globTypeFieldWritersFactory = globTypeFieldWritersFactory;
        for (Map.Entry<GlobType, GlobTypeFieldWriters> globTypeGlobTypeFieldWritersEntry : preInit.entrySet()) {
            globTypeGlobTypeFieldWritersEntry.getKey().init(property, globTypeGlobTypeFieldWritersEntry.getValue());
        }
    }

    public GlobTypeFieldWriters getOrCreate(GlobType globType) {
        return globType.get(property, this);
    }

    @Override
    public GlobTypeFieldWriters create(GlobType globType) {
        return globTypeFieldWritersFactory.create(globType);
    }
}
