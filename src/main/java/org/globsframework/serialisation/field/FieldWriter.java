package org.globsframework.serialisation.field;

import org.globsframework.core.model.Glob;
import org.globsframework.core.model.caller.FromGlobFunction;
import org.globsframework.serialisation.stream.CodedOutputStream;

/**
 * Writes one field of one GlobType, through either of two paths that must produce the same bytes.
 * <p>
 * {@link #write} pulls the value out of the Glob itself, through the accessor the writer holds : that is the
 * path {@link org.globsframework.serialisation.glob.type.GlobTypeFieldWriters} loops over when the type has
 * nothing better to offer. {@code call}, inherited from {@link FromGlobFunction}, is handed the value and
 * the isSet / isNull flags instead, and is what a {@code FromGlobCaller} drives — one call site per
 * field rather than one for the whole loop. Keep the two in step: only {@code null} vs unset drives the
 * choice between writing nothing, a NULL tag, and the value.
 */
public interface FieldWriter extends FromGlobFunction<Object, CodedOutputStream, Void> {
    void write(CodedOutputStream codedOutputStream, Glob data);

    int getFieldNumber();
}
