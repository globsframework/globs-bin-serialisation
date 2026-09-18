package org.globsframework.serialisation.glob.type;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.caller.FromGlobCallerFactory;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public class InitializedGlobTypeFieldWriterFactory {
    private static final Class<?>[] ARGUMENTS = {CodedOutputStream.class};

    public static GlobTypeFieldWriters create(GlobType type, FieldWriter[] fieldWriters) {
        GlobWriter generated = FromGlobCallerFactory.generatedCallerFor("binser.write", type,
                field -> fieldWriters[field.getIndex()], null, GlobWriter.class, FieldWriter.class,
                ARGUMENTS);
        if (generated != null) {
            return new CallerGlobTypeFieldWriters(generated, type);
        }
        else {
            return new ArraysGlobTypeFieldWriters(type, fieldWriters);
        }

    }

}
