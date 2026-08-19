package org.globsframework.serialisation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.GlobTypeBuilderFactory;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.core.metamodel.impl.DefaultGlobFactoryService;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.GlobFactory;
import org.globsframework.core.model.GlobFactoryService;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.model.generate.read.DefaultFunctionCaller;
import org.globsframework.core.model.generate.read.GenerateCaller;
import org.globsframework.core.model.generate.read.GenerateCallerService;
import org.globsframework.core.model.generate.read.GeneratedFunctionCaller;
import org.globsframework.core.model.generate.read.GlobGenerateFactory;
import org.globsframework.core.model.globaccessor.get.GlobGetAccessor;
import org.globsframework.core.model.globaccessor.set.GlobSetAccessor;
import org.globsframework.serialisation.model.FieldNumber;
import org.globsframework.serialisation.model.UnionType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Supplier;

/**
 * The writers go through the GeneratedFunctionCaller when the type's factory offers one, and the bytes must
 * not move : same field numbers, same order, same null-vs-unset behaviour.
 * <p>
 * The caller is exercised without depending on globs-generate — {@link CallerFactoryService} is a factory
 * that offers a plain DefaultFunctionCaller. What this module has to prove is not that the ASM one is fast,
 * it is that FieldWriter.call and FieldWriter.write say the same thing.
 */
public class GeneratedCallerWriterTest {
    private static final String CALLER_SERVICE = CallerFactoryService.class.getName();

    @After
    public void tearDown() {
        System.clearProperty("globs.builder");
        GlobFactoryService.Builder.reset();
        System.clearProperty("globs.caller");
        GenerateCallerService.Builder.reset();
    }

    @Test
    public void theCallerPathIsTakenAndWritesTheSameBytesAsTheLoop() {
        Types looped = buildTypes("Looped", null);
        Types called = buildTypes("Called", CALLER_SERVICE);

        Assert.assertFalse("no caller without the service",
                looped.proto().getGlobFactory() instanceof GlobGenerateFactory);
        Assert.assertTrue("the service is what makes a caller available",
                called.proto().getGlobFactory() instanceof GlobGenerateFactory);

        Assert.assertArrayEquals(write(looped), write(called));
    }

    /**
     * The other way a caller can arrive : not from the type's factory but from the GenerateCallerService of
     * {@code -Dglobs.caller}, which is how globs-generate offers one over a Glob it did not build. Nothing in
     * this module knows about it — going through GenerateCaller rather than testing GlobGenerateFactory by
     * hand is the whole reason it works.
     * <p>
     * The stand-in service counts and delegates, so the assertion is that the writers asked at all; what it
     * hands back is deliberately the looped caller, since this module is not the place to test ASM.
     */
    @Test
    public void theCallerServiceReachesTheWritersAndDoesNotChangeTheBytes() {
        Types types = buildTypes("Service", null);
        byte[] expected = write(types);

        System.setProperty("globs.caller", CountingCallerService.class.getName());
        GenerateCallerService.Builder.reset();
        CountingCallerService.CALLS.set(0);
        try {
            byte[] actual = write(types);
            Assert.assertTrue("the writers never asked the service", CountingCallerService.CALLS.get() > 0);
            Assert.assertArrayEquals(expected, actual);
        } finally {
            System.clearProperty("globs.caller");
            GenerateCallerService.Builder.reset();
        }
    }

    /** ... and what comes back is what went in, so the caller path is not just self-consistent. */
    @Test
    public void theCallerPathRoundTrips() {
        Types called = buildTypes("RoundTrip", CALLER_SERVICE);
        GlobType type = called.proto();
        Glob written = fill(called);

        BinReader reader = BinReaderFactory.create()
                .createFromStream(new ByteArrayInputStream(write(called)));
        Glob read = reader.read(type);

        Assert.assertNotNull(read);
        for (Field field : type.getFields()) {
            Assert.assertEquals(field.getName(), written.isSet(field), read.isSet(field));
            assertSameValue(field, written.getValue(field), read.getValue(field));
        }
    }

    private void assertSameValue(Field field, Object expected, Object actual) {
        if (expected != null && expected.getClass().isArray()) {
            Assert.assertNotNull(field.getName(), actual);
            Assert.assertEquals(field.getName(), Array.getLength(expected), Array.getLength(actual));
            for (int i = 0; i < Array.getLength(expected); i++) {
                Object one = Array.get(expected, i);
                Object two = Array.get(actual, i);
                if (one instanceof Glob) {
                    Assert.assertTrue(field.getName(), ((Glob) one).matches((Glob) two));
                } else {
                    Assert.assertEquals(field.getName(), one, two);
                }
            }
        } else if (expected instanceof Glob) {
            Assert.assertTrue(field.getName(), ((Glob) expected).matches((Glob) actual));
        } else {
            Assert.assertEquals(field.getName(), expected, actual);
        }
    }

    private byte[] write(Types types) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinWriterFactory.create().createFromStream(out).write(fill(types));
        return out.toByteArray();
    }

    /** A value, an explicit null and an untouched field on every kind that has its own writer. */
    private Glob fill(Types types) {
        GlobType type = types.proto();
        MutableGlob glob = type.instantiate()
                .setValue(type.getField("booleanField"), true)
                .setValue(type.getField("booleanArrayField"), new boolean[]{true, false})
                .setValue(type.getField("intField"), 12)
                .setValue(type.getField("intArrayField"), new int[]{1, 2, 3})
                .setValue(type.getField("longField"), 13L)
                .setValue(type.getField("longArrayField"), new long[]{4L, 5L})
                .setValue(type.getField("doubleArrayField"), new double[]{1.5, 2.5})
                .setValue(type.getField("bigDecimalField"), new BigDecimal("12.34"))
                .setValue(type.getField("bigDecimalArrayField"), new BigDecimal[]{new BigDecimal("1.1")})
                .setValue(type.getField("strArrayField"), new String[]{"a", "b"})
                .setValue(type.getField("dateField"), LocalDate.of(2026, Month.AUGUST, 9))
                .setValue(type.getField("dateTimeField"),
                        ZonedDateTime.of(2026, 8, 9, 10, 30, 0, 0, ZoneId.of("Europe/Paris")))
                .setValue(type.getField("bytesField"), new byte[]{1, 2, 3})
                // explicit nulls : written as a NULL tag, unlike the untouched doubleField below
                .setValue(type.getField("strField"), null)
                .setValue(type.getField("globField"), nested(types, 1))
                .setValue(type.getField("globArrayField"), new Glob[]{nested(types, 2), nested(types, 3)})
                .setValue(type.getField("globUnionField"), nested(types, 4))
                .setValue(type.getField("globArrayUnionField"),
                        new Glob[]{nested(types, 5), types.other().instantiate()
                                .setValue(types.other().getField("booleanField"), false)});
        // doubleField is left unset on purpose : nothing at all on the wire
        return glob;
    }

    private Glob nested(Types types, int value) {
        return types.proto().instantiate().setValue(types.proto().getField("intField"), value);
    }

    private record Types(GlobType proto, GlobType other) {
    }

    /** The same shape as BinReaderTest.Proto1, built under whichever GlobFactoryService is asked for. */
    private Types buildTypes(String suffix, String service) {
        if (service == null) {
            System.clearProperty("globs.builder");
        } else {
            System.setProperty("globs.builder", service);
        }
        GlobFactoryService.Builder.reset();

        String otherName = "Other" + suffix;
        GlobTypeBuilder otherBuilder = GlobTypeBuilderFactory.create(otherName);
        otherBuilder.declareBooleanField("booleanField", FieldNumber.create(1));
        GlobType other = otherBuilder.build();

        String protoName = "Proto" + suffix;
        GlobTypeBuilder builder = GlobTypeBuilderFactory.create(protoName);
        builder.declareBooleanField("booleanField", FieldNumber.create(1));
        builder.declareBooleanArrayField("booleanArrayField", FieldNumber.create(2));
        builder.declareIntegerField("intField", FieldNumber.create(3));
        builder.declareIntegerArrayField("intArrayField", FieldNumber.create(4));
        builder.declareLongField("longField", FieldNumber.create(5));
        builder.declareLongArrayField("longArrayField", FieldNumber.create(6));
        builder.declareDoubleField("doubleField", FieldNumber.create(7));
        builder.declareDoubleArrayField("doubleArrayField", FieldNumber.create(8));
        builder.declareBigDecimalField("bigDecimalField", FieldNumber.create(9));
        builder.declareBigDecimalArrayField("bigDecimalArrayField", FieldNumber.create(10));
        builder.declareStringField("strField", FieldNumber.create(11));
        builder.declareStringArrayField("strArrayField", FieldNumber.create(12));
        builder.declareDateField("dateField", FieldNumber.create(13));
        builder.declareDateTimeField("dateTimeField", FieldNumber.create(14));
        builder.declareBytesField("bytesField", FieldNumber.create(15));
        // no field number : NullFieldWriter on the loop path, a no-op call on the caller path
        builder.declareStringField("notSerialised");
        GlobType[] holder = new GlobType[1];
        builder.declareGlobField("globField", () -> holder[0], FieldNumber.create(16));
        builder.declareGlobArrayField("globArrayField", () -> holder[0], FieldNumber.create(17));
        builder.declareGlobUnionField("globUnionField",
                new Supplier[]{() -> holder[0], () -> other}, FieldNumber.create(18),
                UnionType.create(UnionType.ChoiceType.create(protoName, 1),
                        UnionType.ChoiceType.create(otherName, 2)));
        builder.declareGlobUnionArrayField("globArrayUnionField",
                new Supplier[]{() -> holder[0], () -> other}, FieldNumber.create(19),
                UnionType.create(UnionType.ChoiceType.create(protoName, 1),
                        UnionType.ChoiceType.create(otherName, 2)));
        holder[0] = builder.build();

        System.clearProperty("globs.builder");
        GlobFactoryService.Builder.reset();
        return new Types(holder[0], other);
    }

    /** Counts what the writers asked it for, and hands back the looped caller. */
    public static class CountingCallerService implements GenerateCallerService {
        static final java.util.concurrent.atomic.AtomicInteger CALLS = new java.util.concurrent.atomic.AtomicInteger();

        public GenerateCaller getGenerateCaller(GlobType type) {
            return new GenerateCaller() {
                public <D, E> GeneratedFunctionCaller<D, E> create(String name, GetFieldValueFunction<D, E> functions) {
                    GeneratedFunctionCaller<D, E> delegate = new DefaultFunctionCaller<>(type, functions);
                    return (data, ctx1, ctx2) -> {
                        CALLS.incrementAndGet();
                        delegate.call(data, ctx1, ctx2);
                    };
                }
            };
        }
    }

    /**
     * A GlobFactoryService whose factories implement GlobGenerateFactory without generating anything : the
     * globs are core's, and the caller is the looped DefaultFunctionCaller. Enough to put the writers on the
     * caller path, which is what this module is responsible for.
     */
    public static class CallerFactoryService implements GlobFactoryService {
        private final GlobFactoryService delegate = new DefaultGlobFactoryService();

        public GlobFactory getFactory(GlobType type) {
            return new CallerFactory(delegate.getFactory(type));
        }
    }

    private record CallerFactory(GlobFactory delegate) implements GlobGenerateFactory {
        public GlobType getGlobType() {
            return delegate.getGlobType();
        }

        public MutableGlob create(Object context) {
            return delegate.create(context);
        }

        public GlobSetAccessor getSetValueAccessor(Field field) {
            return delegate.getSetValueAccessor(field);
        }

        public GlobGetAccessor getGetValueAccessor(Field field) {
            return delegate.getGetValueAccessor(field);
        }

        public <D, E> GeneratedFunctionCaller<D, E> create(String name, GetFieldValueFunction<D, E> getFieldValueFunction) {
            return new DefaultFunctionCaller<>(getGlobType(), getFieldValueFunction);
        }
    }
}
