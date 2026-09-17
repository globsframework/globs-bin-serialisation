package org.globsframework.serialisation.glob.type;

import org.globsframework.core.model.MutableGlob;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;

/**
 * The read loop of one GlobType, as globs-generate emits it : the switch over the field numbers that
 * {@link GlobTypeFieldReaders} would otherwise walk through its array.
 * <p>
 * This is the interface the generated class implements, and {@link FieldReader} is what it calls — both of
 * them ours, so the emitted code passes the stream and the Glob straight through. The stream is the
 * {@code KeySource} the loop asks for the next field number, which is why it is one of the two arguments
 * rather than a parameter of its own.
 */
public interface GlobFieldsReader {

    void read(MutableGlob data, CodedInputStream inputStream);
}
