package org.globsframework.serialisation.field;

import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.glob.type.GlobWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

/**
 * Writes one field of one GlobType, through either of two paths that must produce the same bytes.
 * <p>
 * {@link #write} pulls the value out of the Glob itself, through the accessor the writer holds : that is the
 * path {@link org.globsframework.serialisation.glob.type.GlobTypeFieldWriters} loops over when the type has
 * nothing better to offer. {@link #call} is handed the value and the isSet / isNull flags instead, and is
 * what a generated {@link GlobWriter} drives — one call site per field rather than one for the whole loop.
 * Keep the two in step: only {@code null} vs unset drives the choice between writing nothing, a NULL tag,
 * and the value.
 * <p>
 * Those four arguments are the whole of it : this is what the caller is generated over, so the emitted class
 * calls {@code call} with its own descriptor. It used to come from core's {@code FromGlobFunction}, whose
 * two contexts were {@code Object} and one of which was always null here.
 */
public interface FieldWriter {
    void write(CodedOutputStream codedOutputStream, Glob data);

    /** The same write, driven by a generated caller : it read isSet, isNull and the value off the Glob. */
    void call(boolean isSet, boolean isNull, Object value, CodedOutputStream codedOutputStream);

    int getFieldNumber();
}
