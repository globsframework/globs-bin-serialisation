package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultGlobTypeFieldWritersManager implements GlobTypeFieldWritersManager {
    private final Map<String, GlobTypeFieldWriters> writersMap = new ConcurrentHashMap<>();

    public GlobTypeFieldWriters getOrCreate(GlobType globType, GlobTypeFieldWritersFactory factory) {
        return writersMap.computeIfAbsent(globType.getName(), s -> factory.create(globType));
    }

}
