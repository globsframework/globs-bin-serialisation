package org.globsframework.serialisation.glob;

import org.globsframework.core.model.Glob;
import org.globsframework.core.utils.serialization.SerializedInput;
import org.globsframework.serialisation.BinReader;
import org.globsframework.serialisation.field.reader.GlobTypeIndexResolver;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;
import org.globsframework.serialisation.stream.CodedInputStream;

import java.io.InputStream;
import java.util.Optional;

public class GlobBinReader implements BinReader {
    private final CodedInputStream codedInputStream;
    private final GlobTypeIndexResolver resolver;

    public GlobBinReader(GlobTypeIndexResolver resolver, GlobTypeFieldReadersManager globTypeFieldReadersManager, InputStream  inputStream) {
        this.resolver = resolver;
        codedInputStream = CodedInputStream.newInstance(globTypeFieldReadersManager, inputStream);
    }

    public GlobBinReader(GlobTypeIndexResolver resolver, GlobTypeFieldReadersManager globTypeFieldReadersManager, SerializedInput serializedInput) {
        this.resolver = resolver;
        codedInputStream = CodedInputStream.newInstance(globTypeFieldReadersManager, serializedInput);
    }

    public GlobBinReader(GlobInstantiator globInstantiator, GlobTypeIndexResolver resolver, GlobTypeFieldReadersManager globTypeFieldReadersManager, SerializedInput serializedInput) {
        this.resolver = resolver;
        codedInputStream = CodedInputStream.newInstance(globInstantiator, globTypeFieldReadersManager, serializedInput);
    }

    public Optional<Glob> read() {
        return Optional.ofNullable(codedInputStream.readGlob(resolver));
    }

    public Glob[] readArray() {
        int size = codedInputStream.readInt();
        Glob[] elements = new  Glob[size];
        for (int i = 0; i < size; i++) {
            elements[i] = codedInputStream.readGlob(resolver);
        }
        return elements;
    }
}
