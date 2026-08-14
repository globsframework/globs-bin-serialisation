package org.globsframework.serialisation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.GlobTypeBuilderFactory;
import org.globsframework.core.metamodel.fields.*;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.utils.ReusableByteArrayOutputStream;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldWritersManager;
import org.globsframework.serialisation.model.FieldNumber;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;

/**
 * Binary serialization of Globs, core's DefaultGlob against the two ASM flavours of globs-generate.
 * <p>
 * The point of the OBJECT / PRIMITIVE arms is the caller : for a generated type, `GlobTypeFieldWriters` gets a
 * `GeneratedFunctionCaller` holding each `FieldWriter` in a static final field, so the write of a field is a
 * monomorphic call instead of the megamorphic one the loop makes over every FieldWriter class. DEFAULT has no
 * caller and takes the loop — it is the control that says the measurement is measuring that.
 * <p>
 * Four *different* GlobTypes per flavour, as in globs-grpc's benchmark of the same name : with a single type
 * the call sites of the loop are monomorphic and the numbers say nothing about a real application. Each shape
 * carries a nested Glob and a nested Glob array of its own type, so the Glob-valued writers — the ones that
 * call something else — are on the hot path too.
 * <p>
 * Run :
 * <pre>
 * mvn -o test-compile dependency:build-classpath -Dmdep.outputFile=/tmp/cp.txt
 * java -cp target/classes:target/test-classes:$(cat /tmp/cp.txt) org.openjdk.jmh.Main GeneratedGlobPerfTest -p flavour=OBJECT
 * </pre>
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 3, time = 3)
@Fork(2)
@State(Scope.Thread)
public class GeneratedGlobPerfTest {
    /** field counts of the four shapes, spanning the 32 / 64 specializations of core and of the generators */
    private static final int[] SHAPES = {6, 15, 28, 45};
    /** levels below the root of the nested shape : 2^(depth+1)-1 = 15 Globs, 60 scalars, 14 descents */
    private static final int NESTED_DEPTH = 3;

    @Param({"DEFAULT", "OBJECT", "PRIMITIVE"})
    public String flavour;

    private GlobType[] types;
    private Glob[] data;
    private BinWriterFactory writerFactory;
    private ReusableByteArrayOutputStream output;

    /** the nested-heavy shape : same total field count, but most of the writing is a descent */
    private GlobType nestedType;
    private Glob nestedData;

    @Setup
    public void setup() {
        GlobFlavour.valueOf(flavour).build(() -> {
            types = new GlobType[SHAPES.length];
            for (int i = 0; i < SHAPES.length; i++) {
                types[i] = declare(flavour + "Shape" + i, SHAPES[i]);
            }
            nestedType = declareNested(flavour + "Nested", NESTED_DEPTH);
            return null;
        });
        data = new Glob[types.length];
        for (int i = 0; i < types.length; i++) {
            data[i] = fill(types[i].instantiate(), i, true);
        }
        nestedData = fillTree(nestedType.instantiate(), 0);
        GlobTypeFieldWritersManager.Builder builder = GlobTypeFieldWritersManager.Builder.init();
        for (GlobType type : types) {
            builder.add(type);
        }
        builder.add(nestedType);
        writerFactory = BinWriterFactory.create(builder.build());
        output = new ReusableByteArrayOutputStream();
    }

    /**
     * What the second level costs : a tree of 15 Globs (a root and four levels of two children each), where
     * every Glob-valued write is a descent through `GlobTypeFieldWriters` — the call this module cannot fold,
     * `caller` being non-final. Against `write`, whose shapes hold two nested Globs among 6 to 45 scalars,
     * this is the ceiling of what making that call foldable could buy.
     */
    @Benchmark
    public void writeNested(Blackhole blackhole) {
        output.reset();
        BinWriter binWriter = writerFactory.createFromStream(output);
        binWriter.getWriter(nestedType).write(nestedData);
        blackhole.consume(output.size());
    }

    /** one pass = the four shapes written once each, nested Globs included */
    @Benchmark
    public void write(Blackhole blackhole) {
        output.reset();
        BinWriter binWriter = writerFactory.createFromStream(output);
        for (int i = 0; i < data.length; i++) {
            binWriter.getWriter(types[i]).write(data[i]);
        }
        blackhole.consume(output.size());
    }

    /** the same, one shape at a time, to see the cost per width rather than the total */
    @Benchmark
    public void writeWidest(Blackhole blackhole) {
        output.reset();
        BinWriter binWriter = writerFactory.createFromStream(output);
        binWriter.getWriter(types[types.length - 1]).write(data[data.length - 1]);
        blackhole.consume(output.size());
    }

    /**
     * A tree of types, {@code depth} levels deep, each level holding four scalars and two Globs of the level
     * below — **acyclic**, unlike the shapes above : a self-recursive type is what forces the two-phase build
     * of `DefaultGlobTypeFieldWritersFactory`, and the point here is to measure the descent, not that.
     */
    private static GlobType declareNested(String name, int depth) {
        GlobType child = null;
        for (int level = depth; level >= 0; level--) {
            final GlobTypeBuilder builder = GlobTypeBuilderFactory.create(name + "_" + level);
            builder.declareStringField("str", FieldNumber.create(1));
            builder.declareIntegerField("int", FieldNumber.create(2));
            builder.declareLongField("lng", FieldNumber.create(3));
            builder.declareDoubleField("dbl", FieldNumber.create(4));
            if (child != null) {
                final GlobType target = child;
                builder.declareGlobField("left", () -> target, FieldNumber.create(5));
                builder.declareGlobField("right", () -> target, FieldNumber.create(6));
            }
            child = builder.build();
        }
        return child;
    }

    private static GlobType declare(String name, int fieldCount) {
        final GlobTypeBuilder builder = GlobTypeBuilderFactory.create(name);
        final GlobType[] self = new GlobType[1];
        for (int i = 0; i < fieldCount - 2; i++) {
            final int number = i + 1;
            switch (i % 10) {
                case 0 -> builder.declareStringField("str" + i, FieldNumber.create(number));
                case 1 -> builder.declareIntegerField("int" + i, FieldNumber.create(number));
                case 2 -> builder.declareLongField("lng" + i, FieldNumber.create(number));
                case 3 -> builder.declareDoubleField("dbl" + i, FieldNumber.create(number));
                case 4 -> builder.declareBooleanField("bool" + i, FieldNumber.create(number));
                case 5 -> builder.declareIntegerField("int2_" + i, FieldNumber.create(number));
                case 6 -> builder.declareIntegerArrayField("intArr" + i, FieldNumber.create(number));
                case 7 -> builder.declareLongArrayField("lngArr" + i, FieldNumber.create(number));
                case 8 -> builder.declareDoubleArrayField("dblArr" + i, FieldNumber.create(number));
                default -> builder.declareStringArrayField("strArr" + i, FieldNumber.create(number));
            }
        }
        builder.declareGlobField("child", () -> self[0], FieldNumber.create(fieldCount - 1));
        builder.declareGlobArrayField("children", () -> self[0], FieldNumber.create(fieldCount));
        self[0] = builder.build();
        return self[0];
    }

    /** the whole tree, every level : 2^(depth+1)-1 Globs, all of them written on each pass */
    static MutableGlob fillTree(MutableGlob glob, int seed) {
        for (Field field : glob.getType().getFields()) {
            final int i = field.getIndex() + seed;
            if (field instanceof StringField f) {
                glob.set(f, "value" + i);
            } else if (field instanceof IntegerField f) {
                glob.set(f, i * 7);
            } else if (field instanceof LongField f) {
                glob.set(f, i * 1_000_003L);
            } else if (field instanceof DoubleField f) {
                glob.set(f, i + 0.5);
            } else if (field instanceof GlobField<?> f) {
                glob.set(f, fillTree(f.getTargetType().instantiate(), seed + i + 1));
            }
        }
        return glob;
    }

    static MutableGlob fill(MutableGlob glob, int seed, boolean withChildren) {
        for (Field field : glob.getType().getFields()) {
            final int i = field.getIndex() + seed;
            if (field instanceof StringField f) {
                glob.set(f, "value" + i);
            } else if (field instanceof IntegerField f) {
                glob.set(f, i * 7);
            } else if (field instanceof LongField f) {
                glob.set(f, i * 1_000_003L);
            } else if (field instanceof DoubleField f) {
                glob.set(f, i + 0.5);
            } else if (field instanceof BooleanField f) {
                glob.set(f, i % 2 == 0);
            } else if (field instanceof IntegerArrayField f) {
                glob.set(f, new int[]{i, i + 1, i + 2});
            } else if (field instanceof LongArrayField f) {
                glob.set(f, new long[]{i, i + 1, i + 2});
            } else if (field instanceof DoubleArrayField f) {
                glob.set(f, new double[]{i + 0.5, i + 1.5});
            } else if (field instanceof StringArrayField f) {
                glob.set(f, new String[]{"a" + i, "b" + i});
            } else if (withChildren && field instanceof GlobField<?> f) {
                glob.set(f, fill(f.getTargetType().instantiate(), seed + 1, false));
            } else if (withChildren && field instanceof GlobArrayField<?> f) {
                glob.set(f, new Glob[]{fill(f.getTargetType().instantiate(), seed + 2, false),
                        fill(f.getTargetType().instantiate(), seed + 3, false)});
            }
        }
        return glob;
    }

    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
                .include(GeneratedGlobPerfTest.class.getSimpleName())
                .build())
                .run();
    }

    static {
        System.setProperty("globsframework.field.no.check", "true");
    }
}
