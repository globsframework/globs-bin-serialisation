package org.globsframework.serialisation.glob;

import org.globsframework.metamodel.GlobTypeResolver;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.BinReader;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;
import org.globsframework.serialisation.stream.CodedInputStream;
import org.globsframework.utils.serialization.SerializedInput;
import org.globsframework.utils.serialization.SerializedInputOutputFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GlobBinReader implements BinReader {
    private final GlobTypeFieldReadersManager globTypeFieldReadersManager;
    private GlobTypeResolver resolver;

    public GlobBinReader(GlobTypeResolver resolver, GlobTypeFieldReadersManager globTypeFieldReadersManager) {
        this.resolver = resolver;
        this.globTypeFieldReadersManager = globTypeFieldReadersManager;
    }

    public Optional<Glob> read(InputStream stream) {
        CodedInputStream inputStream = CodedInputStream.newInstance(globTypeFieldReadersManager, stream);
        return inputStream.readGlob(resolver);
    }

    @Override
    public Optional<Glob> read(byte[] data) {
        CodedInputStream inputStream = CodedInputStream.newInstance(globTypeFieldReadersManager, data);
        return inputStream.readGlob(resolver);
    }

    public Glob[] readArray(InputStream stream) {
        CodedInputStream inputStream = CodedInputStream.newInstance(globTypeFieldReadersManager, stream);
        int size = inputStream.readInt();
        List<Glob> elements = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            read(stream).ifPresent(elements::add);
        }
        return elements.toArray(Glob[]::new);
    }

    @Override
    public Glob[] readArray(byte[] data) {
        CodedInputStream inputStream = CodedInputStream.newInstance(globTypeFieldReadersManager, data);
        int size = inputStream.readInt();
        List<Glob> elements = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            inputStream.readGlob(resolver).ifPresent(elements::add);
        }
        return elements.toArray(Glob[]::new);
    }
}
