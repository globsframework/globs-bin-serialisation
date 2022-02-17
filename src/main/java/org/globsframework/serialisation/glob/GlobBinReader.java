package org.globsframework.serialisation.glob;

import org.globsframework.metamodel.GlobType;
import org.globsframework.model.Glob;
import org.globsframework.model.MutableGlob;
import org.globsframework.serialisation.BinReader;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldReadersFactory;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;
import org.globsframework.serialisation.stream.CodedInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import static org.globsframework.serialisation.WireConstants.Type.END_GLOB;

public class GlobBinReader implements BinReader {
    private final CodedInputStream inputStream;
    private final GlobTypeFieldReadersManager globTypeFieldReadersManager;
    private final GlobTypeFieldReadersFactory globTypeFieldReadersFactory;

    public GlobBinReader(InputStream inputStream, GlobTypeFieldReadersManager globTypeFieldReadersManager) {
        this.inputStream = CodedInputStream.newInstance(inputStream);
        this.globTypeFieldReadersManager = globTypeFieldReadersManager;
        this.globTypeFieldReadersFactory = new DefaultGlobTypeFieldReadersFactory(this);
    }

    public Glob read(GlobType globType) throws IOException {
        return read(List.of(globType));
    }

    public Glob read(Collection<GlobType> globTypes) throws IOException {
        int tag = inputStream.readTag();
        if (WireConstants.getTagWireType(tag) != WireConstants.Type.START_GLOB) {
            throw new RuntimeException("Expecting Glob but got " + tag + " : " + WireConstants.getTagWireType(tag));
        }

        String typeName = inputStream.readUtf8String();
        GlobType globType = globTypes.stream()
                .filter(gType -> gType.getName().equals(typeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Expecting " + globTypes + " but found " + typeName));

        GlobTypeFieldReaders globTypeFieldReaders = globTypeFieldReadersManager.getOrCreate(
                globType, globTypeFieldReadersFactory);
        MutableGlob data = globType.instantiate();

        while (true) {
            int fieldTag = inputStream.readTag();
            int fieldNumber = WireConstants.getTagFieldNumber(fieldTag);
            int tagWireType = WireConstants.getTagWireType(fieldTag);

            if (tagWireType == END_GLOB) {
                return data;
            }

            globTypeFieldReaders.get(fieldNumber)
                    .read(data, fieldTag, tagWireType, inputStream);
        }
    }

}
