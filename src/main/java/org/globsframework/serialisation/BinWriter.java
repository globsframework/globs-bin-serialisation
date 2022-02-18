package org.globsframework.serialisation;

import org.globsframework.model.Glob;

import java.io.IOException;

public interface BinWriter {

    void write(Glob glob);

}
