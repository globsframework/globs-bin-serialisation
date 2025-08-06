package org.globsframework.serialisation;

import org.globsframework.core.utils.serialization.SerializedInput;
import org.globsframework.serialisation.glob.GlobBinReader;
import org.globsframework.serialisation.glob.GlobInstantiator;
import org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldReadersFactory;
import org.globsframework.serialisation.glob.type.manager.DefaultGlobTypeFieldReadersManager;

import java.io.InputStream;

public class BinReaderFactory {
    private final DefaultGlobTypeFieldReadersManager globTypeFieldReadersManager;

    private BinReaderFactory() {
        globTypeFieldReadersManager = new DefaultGlobTypeFieldReadersManager(new DefaultGlobTypeFieldReadersFactory());
    }

    public static BinReaderFactory create() {
        return new BinReaderFactory();
    }

    public BinReader createGlobBinReader(InputStream  inputStream) {
        return new GlobBinReader(globTypeFieldReadersManager, inputStream);
    }

    public BinReader createGlobBinReader(SerializedInput serializedInput) {
        return new GlobBinReader(globTypeFieldReadersManager, serializedInput);
    }

    public BinReader createGlobBinReader(GlobInstantiator globInstantiator,  SerializedInput serializedInput) {
        return new GlobBinReader(globInstantiator, globTypeFieldReadersManager, serializedInput);
    }
}
