package org.globsframework.serialisation.glob.type;

import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.stream.CodedOutputStream;

/**
 * The write pass over one GlobType, as globs-generate emits it : the fields of the Glob read straight out of
 * its own class and handed to the writers, unrolled, instead of the loop {@link CallerGlobTypeFieldWriters} walks.
 * <p>
 * This is the interface the generated class implements, and {@link FieldWriter} is what it calls — both of
 * them ours, so the stream travels as itself. Core only fixes the head of each method : the Glob here,
 * {@code isSet, isNull, value} on the function.
 * <p>
 * Note the argument order, which is core's : the Glob first, unlike {@link FieldWriter#write}.
 */
public interface GlobWriter {

    void write(Glob data, CodedOutputStream codedOutputStream);
}
