package org.globsframework.serialisation;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeBuilderFactory;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.annotations.Target;
import org.globsframework.metamodel.fields.*;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.model.FieldNumber;
import org.globsframework.serialisation.glob.GlobBinReader;
import org.globsframework.serialisation.glob.GlobBinWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class BinReaderTest extends TestCase {

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
            Assert.assertEquals(ex.isSet(field), r.isSet(field));
            Assert.assertEquals(ex.getValue(field), r.getValue(field));
        }
    }

    public static class Proto1 {
        public static GlobType TYPE;

        @FieldNumber(1)
        public static IntegerField intField;
        @FieldNumber(2)
        public static LongField longField;
        @FieldNumber(3)
        public static DoubleField doubleField;
        @FieldNumber(4)
        public static BigDecimalField bigDecimalField;
        @FieldNumber(10)
        public static StringField strField;

        @Target(Proto1.class)
        @FieldNumber(20)
        public static GlobField parent;

        static {
            GlobTypeLoaderFactory.create(Proto1.class).load();
        }
    }
}