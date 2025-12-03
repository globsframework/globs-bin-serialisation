package org.globsframework.serialisation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.utils.serialization.SerializedOutput;
import org.globsframework.serialisation.glob.GlobBinWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldWritersFactory;
import org.globsframework.serialisation.glob.type.manager.DefaultGlobTypeFieldWritersManager;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldWritersManager;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class BinWriterFactory {
    private final GlobTypeFieldWritersManager globTypeFieldWritersManager;

    private BinWriterFactory(GlobTypeFieldWritersManager globTypeFieldWritersManager1) {
        this.globTypeFieldWritersManager = globTypeFieldWritersManager1;
    }

    public static BinWriterFactory create() {
        final ConcurrentHashMap<GlobType, GlobTypeFieldWriters> containers = new ConcurrentHashMap<>();
        return new BinWriterFactory(new DefaultGlobTypeFieldWritersManager(containers,
                new DefaultGlobTypeFieldWritersFactory(containers)));
    }

    public static BinWriterFactory create(GlobTypeFieldWritersManager globTypeFieldWritersManager) {
        return new BinWriterFactory(globTypeFieldWritersManager);
    }

    public BinWriter createFromStream(OutputStream outputStream) {
        return new GlobBinWriter(outputStream, globTypeFieldWritersManager);
    }

    public BinWriter create(SerializedOutput serializedOutput) {
        return new GlobBinWriter(serializedOutput, globTypeFieldWritersManager);
    }
}
