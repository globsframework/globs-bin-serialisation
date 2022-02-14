package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;

public interface GlobTypeFieldWritersManager {

    GlobTypeFieldWriters getOrCreate(GlobType globType, GlobTypeFieldWritersFactory factory);

}
