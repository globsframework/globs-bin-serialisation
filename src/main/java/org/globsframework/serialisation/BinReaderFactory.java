package org.globsframework.serialisation;

import org.globsframework.metamodel.GlobTypeResolver;
import org.globsframework.serialisation.glob.GlobBinReader;
import org.globsframework.serialisation.glob.type.manager.DefaultGlobTypeFieldReadersManager;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;

public class BinReaderFactory {

    private BinReaderFactory() {
    }

    public static BinReaderFactory create() {
        return new BinReaderFactory();
    }

    public GlobBinReader createGlobBinReader(GlobTypeResolver resolver) {
        return new GlobBinReader(resolver, new DefaultGlobTypeFieldReadersManager());
    }
}
