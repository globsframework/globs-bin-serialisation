package org.globsframework.serialisation;

import org.globsframework.metamodel.GlobType;
import org.globsframework.model.Glob;

import java.io.IOException;

public interface BinReader {

    Glob read(GlobType globType) throws IOException;

}
