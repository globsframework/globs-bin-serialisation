package org.globsframework.serialisation.glob;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.Glob;
import org.globsframework.core.utils.serialization.SerializedOutput;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldWritersManager;
import org.globsframework.serialisation.stream.CodedOutputStream;
import org.jspecify.annotations.Nullable;

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
        } else {
            GlobType globType = glob.getType();
            GlobTypeFieldWriters globTypeFieldWriters = globTypeFieldWritersManager.getOrCreate(globType);
            globTypeFieldWriters.write(codedOutputStream, glob);
        }
    }

    public void write(Collection<Glob> globs) {
        if (globs == null) {
            codedOutputStream.writeInt(-1);
        } else {
            codedOutputStream.writeInt(globs.size());
            if (globs.isEmpty()) {
                return;
            }
            GlobTypeFieldWriters globTypeFieldWriters = null;
            for (Glob glob : globs) {
                if (glob == null) {
                    codedOutputStream.writeNull();
                } else {
                    if (globTypeFieldWriters == null) {
                        globTypeFieldWriters = globTypeFieldWritersManager.getOrCreate(glob.getType());
                    }
                    globTypeFieldWriters.write(codedOutputStream, glob);
                }
            }
        }
    }

    @Override
    public GlobWriter getWriter(GlobType type) {
        GlobTypeFieldWriters globTypeFieldWriters = globTypeFieldWritersManager.getOrCreate(type);
        return new GlobWriterImpl(globTypeFieldWriters, codedOutputStream);
    }

    private static class GlobWriterImpl implements GlobWriter {
        private final GlobTypeFieldWriters globTypeFieldWriters;
        private final CodedOutputStream codedOutputStream;

        public GlobWriterImpl(GlobTypeFieldWriters globTypeFieldWriters, CodedOutputStream codedOutputStream) {
            this.globTypeFieldWriters = globTypeFieldWriters;
            this.codedOutputStream = codedOutputStream;
        }

        @Override
        public void write(@Nullable Glob glob) {
            globTypeFieldWriters.write(codedOutputStream, glob);
        }

        @Override
        public void write(@Nullable Collection<@Nullable Glob> globs) {
            if (globs == null) {
                codedOutputStream.writeInt(-1);
            } else {
                codedOutputStream.writeInt(globs.size());
                if (globs.isEmpty()) {
                    return;
                }
                for (Glob glob : globs) {
                    if (glob == null) {
                        codedOutputStream.writeNull();
                    } else {
                        globTypeFieldWriters.write(codedOutputStream, glob);
                    }
                }
            }

        }
    }
}
