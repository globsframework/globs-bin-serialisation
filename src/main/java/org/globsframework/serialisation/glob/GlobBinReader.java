package org.globsframework.serialisation.glob;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.GlobInstantiator;
import org.globsframework.core.utils.serialization.SerializedInput;
import org.globsframework.serialisation.BinReader;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;
import org.globsframework.serialisation.stream.CodedInputStream;

import java.io.InputStream;
import java.util.Optional;

public class GlobBinReader implements BinReader {
    private final CodedInputStream codedInputStream;

    public GlobBinReader(GlobTypeFieldReadersManager globTypeFieldReadersManager, InputStream inputStream) {
        codedInputStream = CodedInputStream.newInstance(globTypeFieldReadersManager, inputStream);
    }

    public GlobBinReader(GlobTypeFieldReadersManager globTypeFieldReadersManager, SerializedInput serializedInput) {
        codedInputStream = CodedInputStream.newInstance(globTypeFieldReadersManager, serializedInput);
    }

    public GlobBinReader(GlobInstantiator globInstantiator, GlobTypeFieldReadersManager globTypeFieldReadersManager, SerializedInput serializedInput) {
        codedInputStream = CodedInputStream.newInstance(globInstantiator, globTypeFieldReadersManager, serializedInput);
    }

    public Optional<Glob> read(GlobType type) {
        return Optional.ofNullable(codedInputStream.readGlob(type));
    }

    public Glob[] readArray(GlobType type) {
        int size = codedInputStream.readInt();
        Glob[] elements = new Glob[size];
        for (int i = 0; i < size; i++) {
            elements[i] = codedInputStream.readGlob(type);
        }
        return elements;
    }
}
