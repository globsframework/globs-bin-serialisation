package org.globsframework.serialisation;

import org.globsframework.core.utils.serialization.SerializedOutput;
import org.globsframework.serialisation.glob.GlobBinWriter;
import org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldWritersFactory;
import org.globsframework.serialisation.glob.type.manager.DefaultGlobTypeFieldWritersManager;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldWritersManager;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class BinWriterFactory {
    private final GlobTypeFieldWritersManager globTypeFieldWritersManager;

    private BinWriterFactory(GlobTypeFieldWritersManager globTypeFieldWritersManager) {
        this.globTypeFieldWritersManager = globTypeFieldWritersManager;
    }

    public static BinWriterFactory create() {
        return new BinWriterFactory(new DefaultGlobTypeFieldWritersManager(Map.of(),
                new DefaultGlobTypeFieldWritersFactory(new HashMap<>())));
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
