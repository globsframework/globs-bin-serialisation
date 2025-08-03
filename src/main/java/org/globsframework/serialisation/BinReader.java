package org.globsframework.serialisation;

import org.globsframework.core.model.Glob;

import java.util.Optional;

public interface BinReader {
    Optional<Glob> read();

    Glob[] readArray();
}
