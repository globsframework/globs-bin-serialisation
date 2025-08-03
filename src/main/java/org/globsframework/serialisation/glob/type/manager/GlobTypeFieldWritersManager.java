package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;

public interface GlobTypeFieldWritersManager {

    GlobTypeFieldWriters getOrCreate(GlobType globType);

}
