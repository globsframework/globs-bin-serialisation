package org.globsframework.serialisation;

import org.globsframework.core.model.Glob;

import java.util.Collection;

public interface BinWriter {

    void write(Glob glob);

    void write(Collection<Glob> glob);

}
