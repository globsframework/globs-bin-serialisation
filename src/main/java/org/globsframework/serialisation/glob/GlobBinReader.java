package org.globsframework.serialisation.glob;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.GlobInstantiator;
import org.globsframework.core.utils.serialization.SerializedInput;
import org.globsframework.serialisation.BinReader;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;
import org.globsframework.serialisation.stream.CodedInputStream;
import org.jspecify.annotations.Nullable;

import java.io.InputStream;

public class GlobBinReader implements BinReader {
    private final CodedInputStream codedInputStream;
    private final GlobTypeFieldReadersManager globTypeFieldReadersManager;

    public GlobBinReader(GlobTypeFieldReadersManager globTypeFieldReadersManager, InputStream inputStream) {
        this.globTypeFieldReadersManager = globTypeFieldReadersManager;
        codedInputStream = CodedInputStream.newInstance(globTypeFieldReadersManager, inputStream);
    }

    public GlobBinReader(GlobTypeFieldReadersManager globTypeFieldReadersManager, SerializedInput serializedInput) {
        this.globTypeFieldReadersManager = globTypeFieldReadersManager;
        codedInputStream = CodedInputStream.newInstance(globTypeFieldReadersManager, serializedInput);
    }

    public GlobBinReader(GlobInstantiator globInstantiator, GlobTypeFieldReadersManager globTypeFieldReadersManager, SerializedInput serializedInput) {
        this.globTypeFieldReadersManager = globTypeFieldReadersManager;
        codedInputStream = CodedInputStream.newInstance(globInstantiator, globTypeFieldReadersManager, serializedInput);
    }

    public Glob read(GlobType type) {
        final GlobTypeFieldReaders readers = type == null ? null : globTypeFieldReadersManager.getOrCreate(type);
        return codedInputStream.readGlob(type, readers);
    }

    public Glob[] readArray(GlobType type) {
        final GlobTypeFieldReaders readers = type == null ? null : globTypeFieldReadersManager.getOrCreate(type);
        return readArrays(type, readers, codedInputStream);
    }

    private static Glob[] readArrays(GlobType type, GlobTypeFieldReaders readers, CodedInputStream codedInputStream) {
        int size = codedInputStream.readInt();
        if (size < 0) {
            return null;
        }
        Glob[] elements = new Glob[size];
        for (int i = 0; i < size; i++) {
            elements[i] = codedInputStream.readGlob(type, readers);
        }
        return elements;
    }

    @Override
    public GlobReader getReader(GlobType type) {
        final GlobTypeFieldReaders readers = globTypeFieldReadersManager.getOrCreate(type);
        return new GlobReaderImpl(type, readers, codedInputStream);
    }

    private static class GlobReaderImpl implements GlobReader {
        private final CodedInputStream codedInputStream;
        private final GlobType type;
        private final GlobTypeFieldReaders readers;

        public GlobReaderImpl(GlobType type, GlobTypeFieldReaders readers, CodedInputStream codedInputStream) {
            this.type = type;
            this.readers = readers;
            this.codedInputStream = codedInputStream;
        }

        @Override
        public @Nullable Glob read() {
            return codedInputStream.readGlob(type, readers);
        }

        @Override
        public @Nullable Glob[] readArray() {
            return readArrays(type, readers, codedInputStream);
        }
    }
}
