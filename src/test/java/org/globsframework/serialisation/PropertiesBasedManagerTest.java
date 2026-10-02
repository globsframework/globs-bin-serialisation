package org.globsframework.serialisation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.GlobTypeBuilderFactory;
import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.model.Glob;
import org.globsframework.serialisation.BinReaderTest.Proto1;
import org.globsframework.serialisation.BinReaderTest.Proto2;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldWritersManager;
import org.globsframework.serialisation.model.FieldNumber;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * The properties-based managers (readers/writers cached on the GlobType through a GlobType.Property)
 * must read and write exactly what the map-based ones do.
 */
public class PropertiesBasedManagerTest {

    @Test
    public void factoriesWriteAndReadSameAsMapBased() {
        final Glob glob = buildProto1();

        final byte[] expected = write(BinWriterFactory.create(), glob);
        final BinWriterFactory propertyWriterFactory = BinWriterFactory.createUseProperties();
        Assert.assertArrayEquals(expected, write(propertyWriterFactory, glob));
        // second call goes through the writers cached on the GlobType
        Assert.assertArrayEquals(expected, write(propertyWriterFactory, glob));

        final BinReaderFactory propertyReaderFactory = BinReaderFactory.createPropertyBased();
        final Glob read = read(propertyReaderFactory, expected, Proto1.TYPE);
        checkProto1(read);
        Assert.assertArrayEquals(expected, write(BinWriterFactory.create(), read));
        Assert.assertArrayEquals(expected, write(BinWriterFactory.create(), read(propertyReaderFactory, expected, Proto1.TYPE)));
    }

    @Test
    public void buildersWriteAndReadSameAsMapBased() {
        final Glob glob = buildProto1();

        final GlobTypeFieldWritersManager writersManager = GlobTypeFieldWritersManager.Builder.init()
                .add(Proto1.TYPE)
                .add(Proto2.TYPE)
                .buildPropertiesBased();
        final GlobTypeFieldReadersManager readersManager = GlobTypeFieldReadersManager.Builder.init()
                .add(Proto1.TYPE)
                .add(Proto2.TYPE)
                .buildPropertiesBased();

        Assert.assertSame(writersManager.getOrCreate(Proto1.TYPE), writersManager.getOrCreate(Proto1.TYPE));
        Assert.assertSame(readersManager.getOrCreate(Proto1.TYPE), readersManager.getOrCreate(Proto1.TYPE));

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BinWriterFactory.create(writersManager).createFromStream(outputStream)
                .getWriter(Proto1.TYPE).write(glob);
        final byte[] bytes = outputStream.toByteArray();
        Assert.assertArrayEquals(write(BinWriterFactory.create(), glob), bytes);

        final Glob read = BinReaderFactory.create(readersManager)
                .createFromStream(new ByteArrayInputStream(bytes))
                .getReader(Proto1.TYPE).read();
        checkProto1(read);
        Assert.assertArrayEquals(bytes, write(BinWriterFactory.create(), read));
    }

    @Test
    public void typeNotDeclaredInBuilderIsBuiltOnFirstUse() {
        final GlobTypeFieldWritersManager writersManager = GlobTypeFieldWritersManager.Builder.init().buildPropertiesBased();
        final GlobTypeFieldReadersManager readersManager = GlobTypeFieldReadersManager.Builder.init().buildPropertiesBased();

        final Glob glob = LazyType.TYPE.instantiate()
                .set(LazyType.id, 42)
                .set(LazyType.name, "lazy");

        final byte[] bytes = write(BinWriterFactory.create(writersManager), glob);
        Assert.assertArrayEquals(write(BinWriterFactory.create(), glob), bytes);
        Assert.assertSame(writersManager.getOrCreate(LazyType.TYPE), writersManager.getOrCreate(LazyType.TYPE));

        final Glob read = read(BinReaderFactory.create(readersManager), bytes, LazyType.TYPE);
        Assert.assertNotNull(read);
        Assert.assertEquals(42, read.get(LazyType.id).intValue());
        Assert.assertEquals("lazy", read.get(LazyType.name));
        Assert.assertSame(readersManager.getOrCreate(LazyType.TYPE), readersManager.getOrCreate(LazyType.TYPE));
    }

    private static Glob buildProto1() {
        return Proto1.TYPE.instantiate()
                .set(Proto1.intField, 7)
                .set(Proto1.strField, "root")
                .set(Proto1.longArrayField, new long[]{1L, 2L, 3L})
                .set(Proto1.globField, Proto1.TYPE.instantiate().set(Proto1.intField, 2))
                .set(Proto1.globArrayField, new Glob[]{
                        Proto1.TYPE.instantiate().set(Proto1.strField, "a"),
                        Proto1.TYPE.instantiate().set(Proto1.strField, "b")})
                .set(Proto1.globUnionField, Proto2.TYPE.instantiate().set(Proto2.booleanField, true));
    }

    private static void checkProto1(Glob read) {
        Assert.assertNotNull(read);
        Assert.assertEquals(7, read.get(Proto1.intField).intValue());
        Assert.assertEquals("root", read.get(Proto1.strField));
        Assert.assertArrayEquals(new long[]{1L, 2L, 3L}, read.get(Proto1.longArrayField));
        Assert.assertEquals(2, read.get(Proto1.globField).get(Proto1.intField).intValue());
        final Glob[] children = read.get(Proto1.globArrayField);
        Assert.assertEquals(2, children.length);
        Assert.assertEquals("b", children[1].get(Proto1.strField));
        final Glob union = read.get(Proto1.globUnionField);
        Assert.assertSame(Proto2.TYPE, union.getType());
        Assert.assertTrue(union.isTrue(Proto2.booleanField));
        Assert.assertFalse(read.isSet(Proto1.doubleField));
    }

    private static byte[] write(BinWriterFactory factory, Glob glob) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        factory.createFromStream(outputStream).write(glob);
        return outputStream.toByteArray();
    }

    private static Glob read(BinReaderFactory factory, byte[] bytes, GlobType type) {
        return factory.createFromStream(new ByteArrayInputStream(bytes)).read(type);
    }

    // only used by typeNotDeclaredInBuilderIsBuiltOnFirstUse, so its property is unset until that test
    public static class LazyType {
        public static final GlobType TYPE;
        public static final IntegerField id;
        public static final StringField name;

        static {
            final GlobTypeBuilder builder = GlobTypeBuilderFactory.create("LazyType");
            id = builder.declareIntegerField("id", FieldNumber.create(1));
            name = builder.declareStringField("name", FieldNumber.create(2));
            TYPE = builder.build();
        }
    }
}
