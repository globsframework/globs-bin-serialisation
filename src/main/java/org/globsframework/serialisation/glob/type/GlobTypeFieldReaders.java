package org.globsframework.serialisation.glob.type;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.generate.write.GeneratedCallerWrite;
import org.globsframework.core.model.generate.write.GeneratedFunctionCallerWrite;
import org.globsframework.core.model.generate.write.MutableFunctionWrite;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.field.reader.UnknownFieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

import java.util.SortedMap;
import java.util.TreeMap;

public final class GlobTypeFieldReaders {
    private final FieldReader[] fieldReaders;

    // Set by initCaller once the array is filled -- it cannot be built in the constructor, since the factory
    // publishes this instance before visiting the fields so that recursive types resolve. Null when nothing
    // can generate one, and then the loop below is the only path.
    private GeneratedCallerWrite<CodedInputStream, Void, Void> caller;

    public GlobTypeFieldReaders(FieldReader[] fieldReaders) {
        this.fieldReaders = fieldReaders;
    }

    /**
     * Asks core for a caller over these readers, which is the whole point of them implementing
     * MutableFunctionWrite : a generated one holds each reader in a static final field and dispatches through
     * a switch on the field number, so reading a field is a monomorphic call instead of the megamorphic one
     * the array lookup makes over every FieldReader class in the process.
     * <p>
     * getGenerated, not get : null means "nobody can generate this", and the array below is a better answer
     * than the looped DefaultFunctionCallerWrite -- an index is cheaper than its binary search, for the same
     * megamorphic call at the end. So this costs nothing when {@code -Dglobs.callerWrite} is unset.
     * <p>
     * The name is the identity of the emitted class, and it has to carry the type : a write-side caller is
     * built from functions alone, so nothing else here tells one type's readers from another's. Constant for
     * a given type, which is what makes the generated class the same one from one run to the next.
     * <p>
     * Must be called after the FieldReader[] is filled, and before the readers are used.
     */
    public void initCaller(GlobType type) {
        GeneratedFunctionCallerWrite factory = GeneratedFunctionCallerWrite.getGenerated();
        if (factory == null) {
            return;
        }
        SortedMap<Integer, MutableFunctionWrite<CodedInputStream, Void, Void>> functions = new TreeMap<>();
        for (int fieldNumber = 0; fieldNumber < fieldReaders.length; fieldNumber++) {
            FieldReader fieldReader = fieldReaders[fieldNumber];
            if (fieldReader != UnknownFieldReader.INSTANCE) {
                functions.put(fieldNumber, fieldReader);
            }
        }
        // an unknown field number is skipped, exactly as the array answers UnknownFieldReader for it
        caller = factory.create("binser.read." + type.getName(), functions, UnknownFieldReader.INSTANCE,
                CodedInputStream.END_OF_GLOB);
    }

    /** null when nothing could generate one : the reader then dispatches through {@link #get} itself. */
    public GeneratedCallerWrite<CodedInputStream, Void, Void> caller() {
        return caller;
    }

    public FieldReader get(int fieldNumber) {
        return fieldNumber < fieldReaders.length ? fieldReaders[fieldNumber] : UnknownFieldReader.INSTANCE;
    }
}
