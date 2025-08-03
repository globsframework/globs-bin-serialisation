package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldWritersFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultGlobTypeFieldWritersManager implements GlobTypeFieldWritersManager {
    private final Map<String, GlobTypeFieldWriters> writersMap = new ConcurrentHashMap<>();
    private final DefaultGlobTypeFieldWritersFactory factory = new DefaultGlobTypeFieldWritersFactory();

    public GlobTypeFieldWriters getOrCreate(GlobType globType) {
        return writersMap.computeIfAbsent(globType.getName(), s -> factory.create(globType));
    }

}
