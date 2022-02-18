package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;

import java.util.Optional;

public interface GlobTypeFieldReadersManager {

    GlobTypeFieldReaders getOrCreate(GlobType globType);

}
