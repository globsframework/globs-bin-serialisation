package org.globsframework.serialisation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.GlobInstantiator;
import org.globsframework.core.utils.serialization.SerializedInput;
import org.globsframework.serialisation.glob.GlobBinReader;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldReadersFactory;
import org.globsframework.serialisation.glob.type.manager.DefaultGlobTypeFieldReadersManager;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BinReaderFactory {
    private final GlobTypeFieldReadersManager globTypeFieldReadersManager;

    private BinReaderFactory(GlobTypeFieldReadersManager globTypeFieldReadersManager) {
        this.globTypeFieldReadersManager = globTypeFieldReadersManager;
    }

    public static BinReaderFactory create(GlobTypeFieldReadersManager globTypeFieldReadersManager) {
        return new BinReaderFactory(globTypeFieldReadersManager);
    }

    public static BinReaderFactory create() {
        final Map<GlobType, GlobTypeFieldReaders> containers = new ConcurrentHashMap<>();
        return new BinReaderFactory(
                new DefaultGlobTypeFieldReadersManager(containers,
                        new DefaultGlobTypeFieldReadersFactory(containers)));
    }

    public BinReader createFromStream(SerializedInput serializedInput) {
        return new GlobBinReader(globTypeFieldReadersManager, serializedInput);
    }

    public BinReader create(GlobInstantiator globInstantiator, SerializedInput serializedInput) {
        return new GlobBinReader(globInstantiator, globTypeFieldReadersManager, serializedInput);
    }

    public BinReader createFromStream(InputStream inputStream) {
        return new GlobBinReader(globTypeFieldReadersManager, inputStream);
    }
}
