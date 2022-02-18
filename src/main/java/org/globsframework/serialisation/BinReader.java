package org.globsframework.serialisation;

import org.globsframework.model.Glob;

import java.io.InputStream;
import java.util.Optional;

public interface BinReader {

    Optional<Glob> read(InputStream inputStream);

    Glob[] readArray(InputStream inputStream);
}
