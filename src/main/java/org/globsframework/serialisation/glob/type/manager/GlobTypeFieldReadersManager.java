package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;

public interface GlobTypeFieldReadersManager {

    GlobTypeFieldReaders getOrCreate(GlobType globType);

}
