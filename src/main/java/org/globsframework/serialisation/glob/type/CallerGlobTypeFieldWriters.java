package org.globsframework.serialisation.glob.type;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.utils.FieldCheck;
import org.globsframework.serialisation.stream.CodedOutputStream;
import org.jspecify.annotations.NullMarked;

/**
 * The write pass of one GlobType through the caller globs-generate emitted for it.
 * <p>
 * What is checked here is the GlobType, with the same FieldCheck.check as the FieldWriter[] loop next door,
 * and not the class of the Glob : the caller opens with a CHECKCAST to the Glob class it was generated over,
 * and a GlobType resolves its GlobFactory once, into a final field, so every Glob that type instantiates has
 * that one class. A Glob of the right type built by something else -- a custom GlobInstantiator, a
 * hand-rolled MutableGlob -- passes the check and fails that cast; there is deliberately no class guard and
 * no loop behind it to catch it.
 * <p>
 * <b>Not a record, and {@code caller} is deliberately not final.</b> A final field lets C2 fold it to a
 * constant once this writer is inlined into its parent, devirtualize the generated GlobWriter behind it and
 * pull its whole body in at every descent site. Nothing fails -- no inlining budget is exceeded, raising
 * LiveNodeCountInliningCutoff changes nothing -- the hot code simply grows : measured on JDK 27-ea,
 * GeneratedGlobPerfTest.writeNested lost 10 % (OBJECT) and 12 % (PRIMITIVE), a nested-only shape 16 %, with
 * perfnorm showing +13 % instructions/op, 5.5x the L1-icache loads, 7 icache misses per op against 0, and 5x
 * the frontend stalls. Non-final, the call stays opaque, the compiled unit stays small, and the same
 * benchmarks sit 2 % above the pre-split baseline.
 */
@NullMarked
public final class CallerGlobTypeFieldWriters implements GlobTypeFieldWriters {
    // read on the hot path : see the class comment before making this final
    private GlobWriter caller;
    private final GlobType type;

    public CallerGlobTypeFieldWriters(GlobWriter caller, GlobType type) {
        this.caller = caller;
        this.type = type;
    }

    @Override
    public void write(CodedOutputStream codedOutputStream, Glob glob) {
        FieldCheck.check(type, glob);
        codedOutputStream.writeStartGlob();
        caller.write(glob, codedOutputStream);
        codedOutputStream.writeEndGlob();
    }
}
