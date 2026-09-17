package org.globsframework.serialisation.glob.type;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.caller.ToGlobCallerFactory;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.field.reader.UnknownFieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

import java.util.SortedMap;
import java.util.TreeMap;

public final class GlobTypeFieldReaders {
    /** what a generated caller is emitted over : both of these are ours, so nothing is adapted */
    private static final Class<?>[] ARGUMENTS = {MutableGlob.class, CodedInputStream.class};

    private final FieldReader[] fieldReaders;

    // Set by initCaller once the array is filled -- it cannot be built in the constructor, since the factory
    // publishes this instance before visiting the fields so that recursive types resolve. Null when nothing
    // can generate one, and then the loop below is the only path.
    private GlobFieldsReader caller;

    public GlobTypeFieldReaders(FieldReader[] fieldReaders) {
        this.fieldReaders = fieldReaders;
    }

    /**
     * Asks core for a caller over these readers : a generated one holds each reader in a static final field
     * and dispatches through a switch on the field number, so reading a field is a monomorphic call instead
     * of the megamorphic one the array lookup makes over every FieldReader class in the process.
     * <p>
     * The two interfaces it is emitted over are {@link GlobFieldsReader} and {@link FieldReader}, ours both,
     * so the emitted class <em>is</em> the read loop of this type and calls each reader with the stream and
     * the Glob passed straight through — no adapter, no bridge, no boxed context. The stream is also the
     * {@code KeySource} the loop asks for the next field number, which is why it is simply one of the two
     * arguments.
     * <p>
     * generated, not get : null means "nobody can generate this", and the array below is a better answer
     * than the looped LoopToGlobCallerFactory -- an index is cheaper than its binary search, for the same
     * megamorphic call at the end, and the loop's typed shape is a reflective Proxy on top of that. So this
     * costs nothing when {@code -Dglobs.caller.toGlob} is unset.
     * <p>
     * The name is the identity of the emitted class, and it has to carry the type : a read-side caller is
     * built from functions alone, so nothing else here tells one type's readers from another's. Constant for
     * a given type, which is what makes the generated class the same one from one run to the next.
     * <p>
     * Must be called after the FieldReader[] is filled, and before the readers are used.
     */
    public void initCaller(GlobType type) {
        ToGlobCallerFactory factory = ToGlobCallerFactory.generated();
        if (factory == null) {
            return;
        }
        SortedMap<Integer, FieldReader> functions = new TreeMap<>();
        for (int fieldNumber = 0; fieldNumber < fieldReaders.length; fieldNumber++) {
            FieldReader fieldReader = fieldReaders[fieldNumber];
            if (fieldReader != UnknownFieldReader.INSTANCE) {
                functions.put(fieldNumber, fieldReader);
            }
        }
        // an unknown field number is skipped, exactly as the array answers UnknownFieldReader for it
        caller = factory.create("binser.read." + type.getName(), functions, UnknownFieldReader.INSTANCE,
                CodedInputStream.END_OF_GLOB, GlobFieldsReader.class, FieldReader.class, ARGUMENTS);
    }

    /** null when nothing could generate one : the reader then dispatches through {@link #get} itself. */
    public GlobFieldsReader caller() {
        return caller;
    }

    public FieldReader get(int fieldNumber) {
        return fieldNumber < fieldReaders.length ? fieldReaders[fieldNumber] : UnknownFieldReader.INSTANCE;
    }
}
