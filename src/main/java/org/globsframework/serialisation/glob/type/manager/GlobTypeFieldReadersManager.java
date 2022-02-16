package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;

public interface GlobTypeFieldReadersManager {

    GlobTypeFieldReaders getOrCreate(GlobType globType, GlobTypeFieldReadersFactory factory);

}
