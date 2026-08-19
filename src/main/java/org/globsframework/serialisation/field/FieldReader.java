package org.globsframework.serialisation.field;

import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.caller.ToGlobFunction;
import org.globsframework.serialisation.stream.CodedInputStream;

/**
 * Reads one field of one GlobType into a MutableGlob, through either of two paths that must set the same
 * thing.
 * <p>
 * {@link #read} is handed the tag its caller has just decoded : that is the path
 * {@link org.globsframework.serialisation.glob.type.GlobTypeFieldReaders} takes when it dispatches through its
 * array. {@code call}, inherited from {@link ToGlobFunction}, is what a {@code ToGlobCaller}
 * drives — one call site per field number rather than one for the whole loop — and reads the tag back from the
 * stream, which is the {@code KeySource} that just read it.
 * <p>
 * Each implementation writes {@code call} out as a one-liner over its own {@code read} rather than inheriting a
 * default here : a default on the interface would delegate through an *interface* call on the very path that
 * exists to remove one (globs-grpc measured that, 229k -> 191k ops/s), where a call on the exact final class is
 * statically bound and free.
 */
public interface FieldReader extends ToGlobFunction<CodedInputStream, Void, Void> {
    void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream);

    int getFieldNumber();

    default void defaultReadCase(String fieldName, int tagWireType) {
        throw new RuntimeException("For " + fieldName + " unexpected type " + tagWireType);
    }
}
