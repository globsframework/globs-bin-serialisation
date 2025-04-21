package org.globsframework.serialisation;

import org.globsframework.core.metamodel.GlobTypeResolver;
import org.globsframework.serialisation.glob.GlobBinReader;
import org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldReadersFactory;
import org.globsframework.serialisation.glob.type.manager.DefaultGlobTypeFieldReadersManager;

public class BinReaderFactory {

    private BinReaderFactory() {
    }

    public static BinReaderFactory create() {
        return new BinReaderFactory();
    }

    public BinReader createGlobBinReader(GlobTypeResolver resolver) {
        return new GlobBinReader(resolver, new DefaultGlobTypeFieldReadersManager(new DefaultGlobTypeFieldReadersFactory()));
    }
}
