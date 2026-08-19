package org.globsframework.serialisation.glob.type;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.caller.FromGlobFunction;
import org.globsframework.core.model.caller.FromGlobCallerFactory;
import org.globsframework.core.model.caller.FromGlobCaller;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

public final class GlobTypeFieldWriters {
    private final FieldWriter[] fieldWriters;

    // Set by initCaller once the array is filled -- it cannot be built in the constructor, since the factory
    // publishes this instance before visiting the fields so that recursive types resolve. Both are null when
    // the type's factory generates nothing, and then the loop below is the only path.
    private FromGlobCaller<CodedOutputStream, Void> caller;
    private Class<?> generatedGlobClass;

    public GlobTypeFieldWriters(FieldWriter[] fieldWriters) {
        this.fieldWriters = fieldWriters;
    }

    /**
     * Asks core for a caller over these writers, which is the whole point of them implementing
     * FromGlobFunction : a generated one holds each writer in a static final field, so the write of a
     * field is a monomorphic call instead of the megamorphic one the loop makes over every FieldWriter class.
     * <p>
     * Through FromGlobCallerFactory rather than by testing CallerGlobFactory here, so that both ways of getting
     * one reach this module : the type's own factory when the Globs are generated, and the
     * FromGlobCallerService of {@code -Dglobs.caller.fromGlob} when they are core's DefaultGlob.
     * <p>
     * generatedCallerFor, not callerFor : null means "nobody can generate this", and the loop below is a
     * better answer than the LoopFromGlobCaller callerFor would hand back — it reads through the typed
     * accessor each writer holds rather than Glob.getValue, and NullFieldWriter makes the fields with no
     * field number free, where the caller would call them.
     * <p>
     * Must be called after the FieldWriter[] is filled, and before the writers are used.
     */
    public void initCaller(GlobType type) {
        // the name is the identity of the emitted class : the purpose only, since generatedCallerFor adds
        // the type it is generating over
        FromGlobCaller<CodedOutputStream, Void> generated = FromGlobCallerFactory.generatedCallerFor(
                "binser.write", type,
                new FromGlobCallerFactory.Functions<CodedOutputStream, Void>() {
                    @SuppressWarnings("unchecked")
                    public <T> FromGlobFunction<T, CodedOutputStream, Void> forField(Field field) {
                        return (FromGlobFunction<T, CodedOutputStream, Void>) fieldWriters[field.getIndex()];
                    }
                });
        if (generated != null) {
            caller = generated;
            // a generated caller reads the fields of one Glob class directly -- the generated one, or the
            // concrete DefaultGlob32/64/128 -- so it only accepts what that type's factory built. A
            // MutableGlob from a custom GlobInstantiator has to take the loop.
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
