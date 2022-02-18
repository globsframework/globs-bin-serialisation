package org.globsframework.serialisation.glob;

import org.globsframework.metamodel.GlobTypeResolver;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.BinReader;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;
import org.globsframework.serialisation.stream.CodedInputStream;

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

    public Glob[] readArray(InputStream stream) {
        CodedInputStream inputStream = CodedInputStream.newInstance(globTypeFieldReadersManager, stream);
        int size = inputStream.readInt();
        List<Glob> elements = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            read(stream).ifPresent(elements::add);
        }
        return elements.toArray(Glob[]::new);
    }
}
