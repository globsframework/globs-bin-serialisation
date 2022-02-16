package org.globsframework.serialisation;

import org.globsframework.serialisation.glob.GlobBinReader;
import org.globsframework.serialisation.glob.type.manager.DefaultGlobTypeFieldReadersManager;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;

import java.io.InputStream;

public class BinReaderFactory {
    private final GlobTypeFieldReadersManager globTypeFieldReadersManager;

    private BinReaderFactory() {
        this.globTypeFieldReadersManager = new DefaultGlobTypeFieldReadersManager();
    }

    public static BinReaderFactory create() {
        return new BinReaderFactory();
    }

    public GlobBinReader createGlobBinReader(InputStream inputStream) {
        return new GlobBinReader(inputStream, globTypeFieldReadersManager);
    }
}
