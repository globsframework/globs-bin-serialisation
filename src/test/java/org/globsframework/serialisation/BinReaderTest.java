package org.globsframework.serialisation;

import junit.framework.TestCase;
import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilderFactory;
import org.globsframework.core.metamodel.GlobTypeLoaderFactory;
import org.globsframework.core.metamodel.GlobTypeResolver;
import org.globsframework.core.metamodel.annotations.Target;
import org.globsframework.core.metamodel.annotations.Targets;
import org.globsframework.core.metamodel.fields.*;
import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.glob.GlobBinReader;
import org.globsframework.serialisation.glob.GlobBinWriter;
import org.globsframework.serialisation.model.FieldNumber_;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;

public class BinReaderTest extends TestCase {

    public void testBoolean() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.booleanField, true);
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.booleanField, null);
        check(withNull, withNull);
    }

    public void testBooleanArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.booleanArrayField, new boolean[]{true, false, true});
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.booleanArrayField, null);
        check(withNull, withNull);
    }

    public void testInteger() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.intField, 2);
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.intField, null);
        check(withNull, withNull);
    }

    public void testIntegerArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.intArrayField, new int[]{10, 20, 30, 40, 50});
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.intArrayField, null);
        check(withNull, withNull);
    }

    public void testLong() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.longField, 5L);
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.longField, null);
        check(withNull, withNull);
    }

    public void testLongArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.longArrayField, new long[]{10L, 20L, 30L, 40L, 50L});
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.longArrayField, null);
        check(withNull, withNull);
    }

    public void testDouble() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.doubleField, 10.0);
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.doubleField, null);
        check(withNull, withNull);
    }

    public void testDoubleArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.doubleArrayField, new double[]{10.0, 20.0, 30.0, 40.0, 50.0});
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.doubleArrayField, null);
        check(withNull, withNull);
    }

    public void testBigDecimal() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.bigDecimalField, BigDecimal.valueOf(35.12));
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.bigDecimalField, null);
        check(withNull, withNull);
    }

    public void testBigDecimalArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.bigDecimalArrayField, new BigDecimal[]{
                        BigDecimal.valueOf(10.15),
                        BigDecimal.valueOf(20.12),
                        BigDecimal.valueOf(30.18)
                });
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.bigDecimalArrayField, null);
        check(withNull, withNull);
    }

    public void testString() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.strField, "a string");
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.strField, null);
        check(withNull, withNull);
    }

    public void testStringArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.strArrayField, new String[]{"first string", "second string", "third string"});
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.strArrayField, null);
        check(withNull, withNull);
    }

    public void testDate() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.dateField, LocalDate.of(2022, Month.FEBRUARY, 16));
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.dateField, null);
        check(withNull, withNull);
    }

    public void testDateTime() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.dateTimeField, ZonedDateTime.now());
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.dateTimeField, null);
        check(withNull, withNull);
    }

    public void testBlob() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.blobField, "blabla".getBytes(StandardCharsets.UTF_8));
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.blobField, null);
        check(withNull, withNull);
    }

    public void testGlob() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.globField, Proto1.TYPE.instantiate()
                        .set(Proto1.intField, 2));
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.globField, null);
        check(withNull, withNull);
    }

    public void testGlobArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.globArrayField, new Glob[]{
                        Proto1.TYPE.instantiate()
                                .set(Proto1.intField, 2)
                });
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.globArrayField, null);
        check(withNull, withNull);
    }

    public void testGlobUnion() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.globUnionField, Proto1.TYPE.instantiate()
                        .set(Proto1.intField, 2));
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.globUnionField, null);
        check(withNull, withNull);
    }

    public void testGlobArrayUnion() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.globArrayUnionField, new Glob[]{
                        Proto1.TYPE.instantiate()
                                .set(Proto1.intField, 2),
                        Proto2.TYPE.instantiate()
                                .set(Proto2.booleanField, true)
                });
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.globArrayField, null);
        check(withNull, withNull);
    }

    private void check(Glob p, Glob ex) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GlobBinWriter binWriter = BinWriterFactory.create().create(byteArrayOutputStream);
        binWriter.write(p);

        GlobType readType = ex.getType();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        GlobBinReader binReader = BinReaderFactory.create().createGlobBinReader(GlobTypeResolver.from(readType));
        Glob r = binReader.read(inputStream).get();

        Field[] fields = r.getType().getFields();
        for (Field field : fields) {
            assertEquals(ex.isSet(field), r.isSet(field));
            Object value1 = ex.getValue(field);
            Object value2 = r.getValue(field);

            if (value1 != null && value2 != null && value1.getClass().isArray() && value2.getClass().isArray()) {
                // compare arrays
                assertEquals(Array.getLength(value1), Array.getLength(value2));
                for (int index = 0; index < Array.getLength(value1); index++) {
                    assertEquals(Array.get(value1, index), Array.get(value2, index));
                }
            } else {
                assertEquals(value1, r.getValue(field));
            }
        }
    }

    public static class Proto1 {
        public static GlobType TYPE;

        @FieldNumber_(1)
        public static BooleanField booleanField;
        @FieldNumber_(2)
        public static BooleanArrayField booleanArrayField;
        @FieldNumber_(3)
        public static IntegerField intField;
        @FieldNumber_(4)
        public static IntegerArrayField intArrayField;
        @FieldNumber_(5)
        public static LongField longField;
        @FieldNumber_(6)
        public static LongArrayField longArrayField;
        @FieldNumber_(7)
        public static DoubleField doubleField;
        @FieldNumber_(8)
        public static DoubleArrayField doubleArrayField;
        @FieldNumber_(9)
        public static BigDecimalField bigDecimalField;
        @FieldNumber_(10)
        public static BigDecimalArrayField bigDecimalArrayField;
        @FieldNumber_(11)
        public static StringField strField;
        @FieldNumber_(12)
        public static StringArrayField strArrayField;
        @FieldNumber_(13)
        public static DateField dateField;
        @FieldNumber_(14)
        public static DateTimeField dateTimeField;
        @FieldNumber_(15)
        public static BlobField blobField;

        @Target(Proto1.class)
        @FieldNumber_(16)
        public static GlobField globField;

        @Target(Proto1.class)
        @FieldNumber_(17)
        public static GlobArrayField globArrayField;

        @Targets({Proto1.class, Proto2.class})
        @FieldNumber_(18)
        public static GlobUnionField globUnionField;

        @Targets({Proto1.class, Proto2.class})
        @FieldNumber_(19)
        public static GlobArrayUnionField globArrayUnionField;

        static {
            GlobTypeLoaderFactory.create(Proto1.class).load();
        }
    }

    public static class Proto2 {
        public static GlobType TYPE;

        @FieldNumber_(1)
        public static BooleanField booleanField;

        static {
            GlobTypeLoaderFactory.create(Proto2.class).load();
        }
    }
}
