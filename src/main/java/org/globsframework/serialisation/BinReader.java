package org.globsframework.serialisation;

import org.globsframework.metamodel.GlobType;
import org.globsframework.model.Glob;

import java.io.IOException;
import java.util.Collection;

public interface BinReader {

    Glob read(GlobType globType) throws IOException;

    Glob read(Collection<GlobType> globTypes) throws IOException;

}
