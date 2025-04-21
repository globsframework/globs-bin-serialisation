package org.globsframework.serialisation;

import org.globsframework.serialisation.glob.GlobBinWriter;
import org.globsframework.serialisation.glob.type.manager.DefaultGlobTypeFieldWritersManager;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldWritersManager;

import java.io.OutputStream;

public class BinWriterFactory {
    private final GlobTypeFieldWritersManager globTypeFieldWritersManager;

    private BinWriterFactory() {
        this.globTypeFieldWritersManager = new DefaultGlobTypeFieldWritersManager();
    }

    public static BinWriterFactory create() {
        return new BinWriterFactory();
    }

    public BinWriter create(OutputStream outputStream) {
        return new GlobBinWriter(outputStream, globTypeFieldWritersManager);
    }

}
