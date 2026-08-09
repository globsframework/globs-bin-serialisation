package org.globsframework.serialisation.glob.type;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.generate.FieldValueFunction;
import org.globsframework.core.model.generate.GenerateCaller;
import org.globsframework.core.model.generate.GeneratedFunctionCaller;
import org.globsframework.core.model.generate.GlobGenerateFactory;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public final class GlobTypeFieldWriters {
    private final FieldWriter[] fieldWriters;

    // Set by initCaller once the array is filled -- it cannot be built in the constructor, since the factory
    // publishes this instance before visiting the fields so that recursive types resolve. Both are null when
    // the type's factory generates nothing, and then the loop below is the only path.
    private GeneratedFunctionCaller<CodedOutputStream, Void> caller;
    private Class<?> generatedGlobClass;

    public GlobTypeFieldWriters(FieldWriter[] fieldWriters) {
        this.fieldWriters = fieldWriters;
    }

    /**
     * Asks the type's factory for a caller over these writers, which is the whole point of them implementing
     * FieldValueFunction : the generated one holds each writer in a static final field, so the write of a
     * field is a monomorphic call instead of the megamorphic one the loop makes over every FieldWriter class.
     * <p>
     * Nothing is asked of a type whose factory generates nothing. GenerateCaller.callerFor would answer a
     * DefaultFunctionCaller there, but its loop is not the same trade as the one below : it reads through
     * Glob.getValue rather than the typed accessor each writer holds, and it calls the writers of the fields
     * that have no field number, which NullFieldWriter makes free here.
     * <p>
     * Must be called after the FieldWriter[] is filled, and before the writers are used.
     */
    public void initCaller(GlobType type) {
        if (type.getGlobFactory() instanceof GlobGenerateFactory generate) {
            caller = generate.create(new GenerateCaller.GetFieldValueFunction<CodedOutputStream, Void>() {
                @SuppressWarnings("unchecked")
                public <T> FieldValueFunction<T, CodedOutputStream, Void> create(Field field) {
                    return (FieldValueFunction<T, CodedOutputStream, Void>) fieldWriters[field.getIndex()];
                }
            });
            // a generated caller reads the fields of its own Glob class directly, so it only accepts what
            // that type's factory built -- a MutableGlob from a custom GlobInstantiator has to take the loop
            generatedGlobClass = type.instantiate().getClass();
        }
    }

    public void write(CodedOutputStream codedOutputStream, Glob glob) {
        if (glob == null) {
            codedOutputStream.writeNull();
        } else {
            codedOutputStream.writeStartGlob();
            if (glob.getClass() == generatedGlobClass) {
                caller.call(glob, codedOutputStream, null);
            } else {
                for (FieldWriter fieldWriter : fieldWriters) {
                    fieldWriter.write(codedOutputStream, glob);
                }
            }
            codedOutputStream.writeEndGlob();
        }
    }
}
