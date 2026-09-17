package org.globsframework.serialisation.field;

import org.globsframework.core.model.MutableGlob;
import org.globsframework.serialisation.glob.type.GlobFieldsReader;
import org.globsframework.serialisation.stream.CodedInputStream;

/**
 * Reads one field of one GlobType into a MutableGlob, through either of two paths that must set the same
 * thing.
 * <p>
 * {@link #read} is handed the tag its caller has just decoded : that is the path
 * {@link org.globsframework.serialisation.glob.type.GlobTypeFieldReaders} takes when it dispatches through its
 * array. {@link #call} is what a generated {@link GlobFieldsReader} drives — one call site per field number
 * rather than one for the whole loop — and reads the tag back from the stream, which is the {@code KeySource}
 * that just read it.
 * <p>
 * Those two arguments are the whole of it : this is what the caller is generated over, so the emitted class
 * calls {@code call} with its own descriptor and there is no adapter, no bridge and no boxed context in
 * between. It used to go through core's {@code ToGlobFunction}, whose three contexts were {@code Object} and
 * two of which were always null here.
 * <p>
 * Each implementation writes {@code call} out as a one-liner over its own {@code read} rather than inheriting a
 * default here : a default on the interface would delegate through an *interface* call on the very path that
 * exists to remove one (globs-grpc measured that, 229k -> 191k ops/s), where a call on the exact final class is
 * statically bound and free.
 */
public interface FieldReader {
    void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream);

    /** The same read, driven by a generated caller : the stream has just read the tag that named this one. */
    void call(MutableGlob data, CodedInputStream inputStream);

    int getFieldNumber();

    default void defaultReadCase(String fieldName, int tagWireType) {
        throw new RuntimeException("For " + fieldName + " unexpected type " + tagWireType);
    }
}
