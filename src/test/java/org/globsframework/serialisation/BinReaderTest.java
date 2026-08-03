package org.globsframework.serialisation;

import junit.framework.TestCase;
import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.GlobTypeBuilderFactory;
import org.globsframework.core.metamodel.annotations.Target;
import org.globsframework.core.metamodel.annotations.Targets;
import org.globsframework.core.metamodel.fields.*;
import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldWritersManager;
import org.globsframework.serialisation.model.*;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Supplier;

public class BinReaderTest extends TestCase {

    public void testBoolean() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.booleanField, true);
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.booleanField, null);
        check(withNull, withNull);
    }

    public void testBooleanArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.booleanArrayField, new boolean[]{true, false, true});
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.booleanArrayField, null);
        check(withNull, withNull);
    }

    public void testInteger() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.intField, 2);
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.intField, null);
        check(withNull, withNull);
    }

    public void testIntegerArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.intArrayField, new int[]{10, 20, 30, 40, 50});
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.intArrayField, null);
        check(withNull, withNull);
    }

    public void testLong() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.longField, 5L);
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.longField, null);
        check(withNull, withNull);
    }

    public void testLongArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.longArrayField, new long[]{10L, 20L, 30L, 40L, 50L});
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.longArrayField, null);
        check(withNull, withNull);
    }

    public void testDouble() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.doubleField, 10.0);
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.doubleField, null);
        check(withNull, withNull);
    }

    public void testDoubleArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.doubleArrayField, new double[]{10.0, 20.0, 30.0, 40.0, 50.0});
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.doubleArrayField, null);
        check(withNull, withNull);
    }

    public void testBigDecimal() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.bigDecimalField, BigDecimal.valueOf(35.12));
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
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

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.bigDecimalArrayField, null);
        check(withNull, withNull);
    }

    public void testString() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.strField, "a string");
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.strField, null);
        check(withNull, withNull);
    }

    public void testStringArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.strArrayField, new String[]{"first string", "second string", "third string"});
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.strArrayField, null);
        check(withNull, withNull);
    }

    public void testDate() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.dateField, LocalDate.of(2022, Month.FEBRUARY, 16));
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.dateField, null);
        check(withNull, withNull);
    }

    public void testDateTime() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.dateTimeField, ZonedDateTime.now());
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.dateTimeField, null);
        check(withNull, withNull);
    }

    private static GlobType createEmptyProto1Type() {
        return GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).build();
    }

    private static GlobType createPartialProto1Type() {
        return GlobTypeBuilderFactory.create(Proto1.TYPE.getName())
                .addUnionGlobArrayField(Proto1.globArrayUnionField.getName(),
                        List.of(Proto1.globArrayUnionField.getAnnotation(FieldNumber.KEY),
                                UnionType.TYPE.instantiate()
                                        .set(UnionType.mapping, new Glob[]{
                                                UnionType.ChoiceType.TYPE.instantiate()
                                                        .set(UnionType.ChoiceType.index, 2)
                                                        .set(UnionType.ChoiceType.typeName, Proto2.TYPE.getName())
                                        })), new Supplier[]{() -> Proto2.TYPE})
                .build();
    }

    public void testBytes() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.BytesField, "blabla".getBytes(StandardCharsets.UTF_8));
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.BytesField, null);
        check(withNull, withNull);
    }

    public void testGlob() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.globField, Proto1.TYPE.instantiate()
                        .set(Proto1.intField, 2));
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.globField, null);
        check(withNull, withNull);
    }

    public void testSkip() {
        Glob p1 = Proto1.TYPE.instantiate().set(Proto1.intField, 1);
        Glob p2 = Proto1.TYPE.instantiate().set(Proto1.intField, 2);
        final BinWriterFactory binWriterFactory = BinWriterFactory.create();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinWriter binWriter = binWriterFactory.createFromStream(byteArrayOutputStream);
        binWriter.write(p1);
        binWriter.write(p2);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BinReader binReader = BinReaderFactory.create().createFromStream(inputStream);
        Assert.assertNull(binReader.read(null));
        final Glob read = binReader.read(Proto1.TYPE);
        Assert.assertNotNull(read);
        Assert.assertEquals(2, read.getValue(Proto1.intField));
    }

    public void testGlobArray() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.globArrayField, new Glob[]{
                        Proto1.TYPE.instantiate()
                                .set(Proto1.intField, 2)
                });
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
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

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.globUnionField, null);
        check(withNull, withNull);
    }

    public void testGlobUnionWithPartial() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.globArrayUnionField, new Glob[]{Proto1.TYPE.instantiate()
                        .set(Proto1.intField, 2),
                        Proto2.TYPE.instantiate()
                                .set(Proto2.booleanField, false)});
        check(p, p);

        final GlobType globType = createPartialProto1Type();

        final BinWriterFactory binWriterFactory = BinWriterFactory.create();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinWriter binWriter = binWriterFactory.createFromStream(byteArrayOutputStream);
        binWriter.write(p);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BinReader binReader = BinReaderFactory.create().createFromStream(inputStream);
        Glob r = binReader.read(globType);
        assertNotNull(r);
        final GlobArrayUnionField globUnionField = globType.getField(Proto1.globArrayUnionField.getName()).asGlobArrayUnionField();
        Assert.assertTrue(r.isSet(globUnionField));
        final Glob[] actual = r.get(globUnionField);
        Assert.assertEquals(2, actual.length);
        Assert.assertNull(actual[0]);
        Assert.assertNotNull(actual[1]);
    }

    public void testGlobArrayWithNull() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.globArrayUnionField, new Glob[]{
                        null,
                        Proto2.TYPE.instantiate()
                                .set(Proto2.booleanField, true)
                });
        check(p, p);

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.globArrayField, null);
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

        final GlobType globType = createEmptyProto1Type();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.globArrayField, null);
        check(withNull, withNull);
    }

    private void check(Glob p, Glob ex) throws IOException {
        final BinWriterFactory binWriterFactory = BinWriterFactory.create();
        check(p, ex, binWriterFactory, new ByteArrayOutputStream());
        check(p, ex, binWriterFactory, new ByteArrayOutputStream());
    }

    private static void check(Glob p, Glob ex, BinWriterFactory binWriterFactory, ByteArrayOutputStream byteArrayOutputStream) {
        BinWriter binWriter = binWriterFactory.createFromStream(byteArrayOutputStream);
        binWriter.write(p);

        GlobType readType = ex.getType();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BinReader binReader = BinReaderFactory.create().createFromStream(inputStream);
        Glob r = binReader.read(readType);
        assertNotNull(r);
        Field[] fields = readType.getFields();
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

    public void testUsingBuilder() {
        final GlobTypeFieldWritersManager fieldWritersManager = GlobTypeFieldWritersManager.Builder.init()
                .add(Proto1.TYPE)
                .add(Proto2.TYPE)
                .build();

        final BinWriterFactory binWriterFactory = BinWriterFactory.create(fieldWritersManager);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final BinWriter binWriter = binWriterFactory.createFromStream(outputStream);
        final BinWriter.GlobWriter writer = binWriter.getWriter(Proto1.TYPE);
        writer.write(Proto1.TYPE.instantiate()
                .set(Proto1.intField, 1));

        GlobTypeFieldReadersManager readersManager = GlobTypeFieldReadersManager.Builder.init()
                .add(Proto1.TYPE)
                .add(Proto2.TYPE)
                .build();

        BinReader binReader = BinReaderFactory.create(readersManager).createFromStream(new ByteArrayInputStream(outputStream.toByteArray()));
        Glob read = binReader.read(Proto1.TYPE);
        assertNotNull(read);
        assertEquals(1, read.get(Proto1.intField).intValue());

    }

    public static class Proto1 {
        public static final GlobType TYPE;
        public static final String NAME = "Proto1";

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
        public static BytesField BytesField;

        @Target(Proto1.class)
        @FieldNumber_(16)
        public static GlobField<Proto1> globField;

        @Target(Proto1.class)
        @FieldNumber_(17)
        public static GlobArrayField<Proto1> globArrayField;

        @Targets({Proto1.class, Proto2.class})
        @FieldNumber_(18)
        @UnionType_({
                @UnionType_.ChoiceType_(value = Proto1.class, index = 1),
                @UnionType_.ChoiceType_(value = Proto2.class, index = 2)})
        public static GlobUnionField globUnionField;

        @Targets({Proto1.class, Proto2.class})
        @FieldNumber_(19)
        @UnionType_({
                @UnionType_.ChoiceType_(value = Proto1.class, index = 1),
                @UnionType_.ChoiceType_(value = Proto2.class, index = 2)})
        public static GlobArrayUnionField globArrayUnionField;

        static {
            GlobTypeBuilder typeBuilder = GlobTypeBuilderFactory.create(NAME);
            booleanField = typeBuilder.declareBooleanField("booleanField", FieldNumber.create(1));
            booleanArrayField = typeBuilder.declareBooleanArrayField("booleanArrayField", FieldNumber.create(2));
            intField = typeBuilder.declareIntegerField("intField", FieldNumber.create(3));
            intArrayField = typeBuilder.declareIntegerArrayField("intArrayField", FieldNumber.create(4));
            longField = typeBuilder.declareLongField("longField", FieldNumber.create(5));
            longArrayField = typeBuilder.declareLongArrayField("longArrayField", FieldNumber.create(6));
            doubleField = typeBuilder.declareDoubleField("doubleField", FieldNumber.create(7));
            doubleArrayField = typeBuilder.declareDoubleArrayField("doubleArrayField", FieldNumber.create(8));
            bigDecimalField = typeBuilder.declareBigDecimalField("bigDecimalField", FieldNumber.create(9));
            bigDecimalArrayField = typeBuilder.declareBigDecimalArrayField("bigDecimalArrayField", FieldNumber.create(10));
            strField = typeBuilder.declareStringField("strField", FieldNumber.create(11));
            strArrayField = typeBuilder.declareStringArrayField("strArrayField", FieldNumber.create(12));
            dateField = typeBuilder.declareDateField("dateField", FieldNumber.create(13));
            dateTimeField = typeBuilder.declareDateTimeField("dateTimeField", FieldNumber.create(14));
            BytesField = typeBuilder.declareBytesField("BytesField", FieldNumber.create(15));
            globField = typeBuilder.declareGlobField("globField", () -> Proto1.TYPE, FieldNumber.create(16));
            globArrayField = typeBuilder.declareGlobArrayField("globArrayField", () -> Proto1.TYPE, FieldNumber.create(17));
            globUnionField = typeBuilder.declareGlobUnionField("globUnionField", new Supplier[]{() -> Proto1.TYPE, () -> Proto2.TYPE}, FieldNumber.create(18),
                    UnionType.create(UnionType.ChoiceType.create(NAME, 1), UnionType.ChoiceType.create(Proto2.NAME, 2)));
            globArrayUnionField = typeBuilder.declareGlobUnionArrayField("globArrayUnionField", new Supplier[]{() -> Proto1.TYPE, () -> Proto2.TYPE}, FieldNumber.create(19),
                    UnionType.create(UnionType.ChoiceType.create(NAME, 1), UnionType.ChoiceType.create(Proto2.NAME, 2)));
            TYPE = typeBuilder.build();
        }
    }

    public static class Proto2 {
        public static final GlobType TYPE;
        public static final String NAME = "Proto2";

        @FieldNumber_(1)
        public static BooleanField booleanField;

        static {
            GlobTypeBuilder globTypeBuilder =  GlobTypeBuilderFactory.create(NAME);
            booleanField = globTypeBuilder.declareBooleanField("booleanField", FieldNumber.create(1));
            TYPE = globTypeBuilder.build();
        }
    }
}
