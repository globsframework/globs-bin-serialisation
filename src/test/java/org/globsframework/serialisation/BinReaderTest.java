package org.globsframework.serialisation;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeBuilderFactory;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.annotations.Target;
import org.globsframework.metamodel.fields.GlobField;
import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.model.Glob;
import org.globsframework.serialisation.model.FieldNumber_;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BinReaderTest extends TestCase {


    public void testSimpleInt() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.intField, 2);
        check(p, p);
        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());

        Glob withNull = Proto1.TYPE.instantiate().set(Proto1.intField, null);
        check(withNull, withNull);
    }

    public void testGlob() throws IOException {
        Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.parent, Proto1.TYPE.instantiate().set(Proto1.intField, 2));
        check(p, p);
        GlobType globType = GlobTypeBuilderFactory.create(Proto1.TYPE.getName()).get();
        check(p, globType.instantiate());
    }

    private void check(Glob p, Glob ex) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinWriter.GlobWriter binWriter = BinWriter.create().create(byteArrayOutputStream);
        binWriter.write(p);

        GlobType readType = ex.getType();
        BinReader.GlobBinReader binReader = BinReader.create().create(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        Glob r = binReader.read(readType);

        Field[] fields = r.getType().getFields();
        for (Field field : fields) {
            Assert.assertEquals(ex.isSet(field), r.isSet(field));
            Assert.assertEquals(ex.getValue(field), r.getValue(field));
        }
    }

    public static class Proto1 {
        public static GlobType TYPE;

//        public static StringField strField;
        @FieldNumber_(1)
        public static IntegerField intField;
//        public static DoubleField doubleField;

        @Target(Proto1.class)
        @FieldNumber_(2)
        public static GlobField parent;

        static {
            GlobTypeLoaderFactory.create(Proto1.class).load();
        }
    }
}