package org.globsframework.serialisation.glob.type.factory;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;

public interface GlobTypeFieldWritersFactory {

    GlobTypeFieldWriters create(GlobType type);

}
