package org.globsframework.serialisation;

import junit.framework.TestCase;
import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeBuilderFactory;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.annotations.Target;
import org.globsframework.metamodel.fields.*;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.glob.GlobBinReader;
import org.globsframework.serialisation.glob.GlobBinWriter;
import org.globsframework.serialisation.model.FieldNumber;

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

    public void testSimpleInt() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.intField, 2);
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate()
                .set(Proto1.intField, null);
        check(withNull, withNull);
    }

    public void testLong() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.longField, 5L);
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate().set(Proto1
                .longField, null);
        check(withNull, withNull);
    }

    public void testDouble() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.doubleField, 10.0);
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate().set(Proto1
                .doubleField, null);
        check(withNull, withNull);
    }

    public void testBigDecimal() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.bigDecimalField, BigDecimal.valueOf(35.12));
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate().set(Proto1
                .bigDecimalField, null);
        check(withNull, withNull);
    }

    public void testString() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.strField, "a string");
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate().set(Proto1
                .strField, null);
        check(withNull, withNull);
    }

    public void testDate() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.dateField, LocalDate.of(2022, Month.FEBRUARY, 16));
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate().set(Proto1
                .dateField, null);
        check(withNull, withNull);
    }

    public void testDateTime() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.dateTimeField, ZonedDateTime.now());
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate().set(Proto1
                .dateTimeField, null);
        check(withNull, withNull);
    }

    public void testBlob() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.blobField, "blabla".getBytes(StandardCharsets.UTF_8));
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate().set(Proto1
                .blobField, null);
        check(withNull, withNull);
    }

    public void testGlob() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.parent, Proto1.TYPE.instantiate()
                        .set(Proto1.intField, 2));
        check(p, p);

        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());
    }

    private void check(Glob p, Glob ex) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GlobBinWriter binWriter = BinWriterFactory.create().create(byteArrayOutputStream);
        binWriter.write(p);

        GlobType readType = ex.getType();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        GlobBinReader binReader = BinReaderFactory.create().createGlobBinReader(inputStream);
        Glob r = binReader.read(readType);

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

        @FieldNumber(1)
        public static BooleanField booleanField;
        @FieldNumber(3)
        public static IntegerField intField;
        @FieldNumber(5)
        public static LongField longField;
        @FieldNumber(7)
        public static DoubleField doubleField;
        @FieldNumber(9)
        public static BigDecimalField bigDecimalField;
        @FieldNumber(11)
        public static StringField strField;
        @FieldNumber(13)
        public static DateField dateField;
        @FieldNumber(14)
        public static DateTimeField dateTimeField;
        @FieldNumber(15)
        public static BlobField blobField;

        @Target(Proto1.class)
        @FieldNumber(16)
        public static GlobField parent;

        static {
            GlobTypeLoaderFactory.create(Proto1.class).load();
        }
    }
}