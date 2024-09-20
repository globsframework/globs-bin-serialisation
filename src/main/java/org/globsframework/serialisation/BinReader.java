package org.globsframework.serialisation;

import org.globsframework.core.model.Glob;

import java.io.InputStream;
import java.util.Optional;

public interface BinReader {

    Optional<Glob> read(InputStream inputStream);

    Optional<Glob> read(byte[] data);

    Glob[] readArray(InputStream inputStream);

    Glob[] readArray(byte[] data);
}
