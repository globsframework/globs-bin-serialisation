package org.globsframework.serialisation;

import org.globsframework.model.Glob;

import java.io.IOException;
import java.util.Collection;

public interface BinWriter {

    void write(Glob glob);

    void write(Collection<Glob> glob);

}
