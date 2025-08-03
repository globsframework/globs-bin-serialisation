package org.globsframework.serialisation;

import org.globsframework.core.utils.serialization.SerializedInput;
import org.globsframework.serialisation.field.reader.GlobTypeIndexResolver;
import org.globsframework.serialisation.glob.GlobBinReader;
import org.globsframework.serialisation.glob.GlobInstantiator;
import org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldReadersFactory;
import org.globsframework.serialisation.glob.type.manager.DefaultGlobTypeFieldReadersManager;

import java.io.InputStream;

public class BinReaderFactory {
    private final GlobTypeIndexResolver resolver;
    private final DefaultGlobTypeFieldReadersManager globTypeFieldReadersManager;

    private BinReaderFactory(GlobTypeIndexResolver resolver) {
        this.resolver = resolver;
        globTypeFieldReadersManager = new DefaultGlobTypeFieldReadersManager(new DefaultGlobTypeFieldReadersFactory());
    }

    public static BinReaderFactory create(GlobTypeIndexResolver resolver) {
        return new BinReaderFactory(resolver);
    }

    public BinReader createGlobBinReader(InputStream  inputStream) {
        return new GlobBinReader(resolver, globTypeFieldReadersManager, inputStream);
    }

    public BinReader createGlobBinReader(SerializedInput serializedInput) {
        return new GlobBinReader(resolver, globTypeFieldReadersManager, serializedInput);
    }

    public BinReader createGlobBinReader(GlobInstantiator globInstantiator,  SerializedInput serializedInput) {
        return new GlobBinReader(globInstantiator, resolver, globTypeFieldReadersManager, serializedInput);
    }
}
