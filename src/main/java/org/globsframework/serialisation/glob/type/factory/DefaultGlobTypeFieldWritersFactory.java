package org.globsframework.serialisation.glob.type.factory;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.field.writer.FieldWriterVisitorCreator;
import org.globsframework.serialisation.field.writer.NullFieldWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.InitializedGlobTypeFieldWriterFactory;
import org.globsframework.serialisation.model.FieldNumber;
import org.globsframework.serialisation.stream.CodedOutputStream;

import java.util.*;

public class DefaultGlobTypeFieldWritersFactory implements GlobTypeFieldWritersFactory {
    private final Map<GlobType, GlobTypeFieldWriters> containers;
    private final Set<GlobType> onGoing = new HashSet<>();
    private final Map<GlobType, Delegate> recursives = new HashMap<>();

    public DefaultGlobTypeFieldWritersFactory(Map<GlobType, GlobTypeFieldWriters> containers) {
        this.containers = containers;
    }

    public synchronized GlobTypeFieldWriters create(GlobType type) {
        if (containers.containsKey(type)) {
            return containers.get(type);
        }
        if (!onGoing.add(type)) {
            return recursives.computeIfAbsent(type, globType -> new Delegate());
        }

        Field[] fields = type.getFields();
        FieldWriter[] realFieldWriters = new FieldWriter[fields.length];

        Arrays.fill(realFieldWriters, NullFieldWriter.INSTANCE);

        final FieldWriterVisitorCreator fieldWriterVisitorCreator =
                new FieldWriterVisitorCreator(realFieldWriters, this);
        for (Field field : fields) {
            int ind = field.findOptAnnotation(FieldNumber.KEY)
                    .map(FieldNumber.fieldNumber)
                    .orElse(-1);
            if (ind != -1) {
                field.safeAccept(fieldWriterVisitorCreator, ind);
            }
        }

        final GlobTypeFieldWriters globTypeFieldWriters = InitializedGlobTypeFieldWriterFactory.create(type, realFieldWriters);
        containers.put(type, globTypeFieldWriters);
        onGoing.remove(type);
        final Delegate delegate = recursives.remove(type);
        if (delegate != null) {
            delegate.set(globTypeFieldWriters);
        }
        return globTypeFieldWriters;
    }

    static final class Delegate implements GlobTypeFieldWriters {
        private GlobTypeFieldWriters globTypeFieldWriters;

        @Override
        public void write(CodedOutputStream codedOutputStream, Glob glob) {
            globTypeFieldWriters.write(codedOutputStream, glob);
        }

        public void set(GlobTypeFieldWriters globTypeFieldWriters) {
            this.globTypeFieldWriters = globTypeFieldWriters;
        }
    }
}
