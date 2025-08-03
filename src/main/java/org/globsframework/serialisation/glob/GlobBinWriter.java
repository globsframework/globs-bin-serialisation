package org.globsframework.serialisation.glob;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.Glob;
import org.globsframework.core.utils.serialization.SerializedOutput;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldWritersManager;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.io.OutputStream;
import java.util.Collection;

public class GlobBinWriter implements BinWriter {
    private final CodedOutputStream codedOutputStream;
    private final GlobTypeFieldWritersManager globTypeFieldWritersManager;

    public GlobBinWriter(OutputStream outputStream, GlobTypeFieldWritersManager globTypeFieldWritersManager) {
        this.codedOutputStream = CodedOutputStream.newInstance(outputStream);
        this.globTypeFieldWritersManager = globTypeFieldWritersManager;
    }

    public GlobBinWriter(SerializedOutput serializedOutput, GlobTypeFieldWritersManager globTypeFieldWritersManager) {
        this.codedOutputStream = CodedOutputStream.newInstance(serializedOutput);
        this.globTypeFieldWritersManager = globTypeFieldWritersManager;
    }

    public void write(Glob glob) {
        if (glob == null) {
            codedOutputStream.writeNull();
        }
        else {
            GlobType globType = glob.getType();

            GlobTypeFieldWriters globTypeFieldWriters = globTypeFieldWritersManager.getOrCreate(globType);

            codedOutputStream.writeStartGlob(globTypeFieldWriters.getGlobIndex());
            for (FieldWriter fieldWriter : globTypeFieldWriters.getFieldWriters()) {
                fieldWriter.write(codedOutputStream, glob, this);
            }
            codedOutputStream.writeEndGlob();
        }
    }

    public void write(Collection<Glob> globs) {
        codedOutputStream.writeInt(globs.size());
        globs.forEach(this::write);
    }

}
