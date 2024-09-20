package org.globsframework.serialisation;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.AllAnnotations;
import org.globsframework.core.metamodel.fields.DoubleField;
import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.metamodel.impl.DefaultGlobModel;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Glob;
import org.globsframework.core.utils.ReusableByteArrayOutputStream;
import org.globsframework.core.utils.serialization.SerializedInput;
import org.globsframework.core.utils.serialization.SerializedInputOutputFactory;
import org.globsframework.core.utils.serialization.SerializedOutput;
import org.globsframework.serialisation.glob.GlobBinWriter;
import org.globsframework.serialisation.model.FieldNumber;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PerfReadWriteTest {

    static {
        System.setProperty("org.globsframework.builder", "org.globsframework.model.generator.GeneratorGlobFactoryService");
        System.setProperty("globsframework.field.no.check", "true");
    }

    @Test
    public void perfStandard() throws IOException {
        GlobTypeBuilder globTypeBuilder = DefaultGlobTypeBuilder.init("perf");
        StringField str_1 = globTypeBuilder.declareStringField("str_1", FieldNumber.create(1));
        StringField str_2 = globTypeBuilder.declareStringField("str_2", FieldNumber.create(2));
        IntegerField anInt = globTypeBuilder.declareIntegerField("anInt", FieldNumber.create(3));
        DoubleField aDouble = globTypeBuilder.declareDoubleField("aDouble", FieldNumber.create(4));

        GlobType globType = globTypeBuilder.get();

        List<Glob> collect = IntStream.range(0, 1000)
                .mapToObj(i ->
                        globType.instantiate()
                                .set(str_1, "str_1_" + i)
                                .set(str_2, "str_2_" + i)
                                .set(anInt, i)
                                .set(aDouble, i))
                .collect(Collectors.toList());
        DefaultGlobModel globTypes = new DefaultGlobModel(AllAnnotations.MODEL, globType);

        byte[] s;
        writeBin(collect);
        writeBin(collect);
        writeBin(collect);
        writeBin(collect);
        writeBin(collect);
        writeBin(collect);
        s = writeBin(collect);
        readBin(s, globTypes);
        readBin(s, globTypes);
        readBin(s, globTypes);
        readBin(s, globTypes);
        readBin(s, globTypes);
        readBin(s, globTypes);
        readBin(s, globTypes);
        readBin(s, globTypes);

    }

    private byte[] writeBin(List<Glob> collect) {
        long start = System.nanoTime();
        ReusableByteArrayOutputStream outputStream = null;
        for (int i = 0; i < 1000; i++) {
            outputStream = new ReusableByteArrayOutputStream();
            final SerializedOutput init = SerializedInputOutputFactory.init(outputStream);
            for (Glob glob : collect) {
                init.writeGlob(glob);
            }
        }
        long end = System.nanoTime();
        System.out.println("write " + (end - start) / 1000000. + "ms size : " + outputStream.size() + " s=" + new String(outputStream.getBuffer(), 0, 10));  // 1100ms puis 600ms
        return outputStream.getBuffer();
    }

    private void readBin(byte[] s, DefaultGlobModel globTypes) {
        long start = System.nanoTime();
        Glob[] globs = new Glob[1000];
        for (int i = 0; i < 1000; i++) {
            SerializedInput input = SerializedInputOutputFactory.init(s);
            for (int j = 0; j < 1000; j++) {
                globs[j] = input.readGlob(globTypes);
            }
            Assert.assertEquals(globs.length, 1000);
        }
        long end = System.nanoTime();
        System.out.println("read " + (end - start) / 1000000. + "ms => " + ((1000. * 1000.) / ((end - start) / 1000000.) * 1000.) + " objects/s");  // 600ms (1.7Millions par second)
        System.out.println(globs[10].toString());
    }

    @Test
    public void perf() {
        GlobTypeBuilder globTypeBuilder = DefaultGlobTypeBuilder.init("perf");
        StringField str_1 = globTypeBuilder.declareStringField("str_1", FieldNumber.create(1));
        StringField str_2 = globTypeBuilder.declareStringField("str_2", FieldNumber.create(2));
        IntegerField anInt = globTypeBuilder.declareIntegerField("anInt", FieldNumber.create(3));
        DoubleField aDouble = globTypeBuilder.declareDoubleField("aDouble", FieldNumber.create(4));

        GlobType globType = globTypeBuilder.get();

        List<Glob> collect = IntStream.range(0, 1000)
                .mapToObj(i ->
                        globType.instantiate()
                                .set(str_1, "str_1_" + i)
                                .set(str_2, "str_2_" + i)
                                .set(anInt, i)
                                .set(aDouble, i))
                .collect(Collectors.toList());
        DefaultGlobModel globTypes = new DefaultGlobModel(AllAnnotations.MODEL, globType);
        BinReader globBinReader = BinReaderFactory.create().createGlobBinReader(globTypes);
        BinWriterFactory binWriterFactory = BinWriterFactory.create();
        byte[] s;
        write(collect, binWriterFactory);
        write(collect, binWriterFactory);
        write(collect, binWriterFactory);
        write(collect, binWriterFactory);
        write(collect, binWriterFactory);
        write(collect, binWriterFactory);
        s = write(collect, binWriterFactory);
        read(globBinReader, s);
        read(globBinReader, s);
        read(globBinReader, s);
        read(globBinReader, s);
        read(globBinReader, s);
        read(globBinReader, s);
        read(globBinReader, s);
        read(globBinReader, s);
    }

    /*
    write 271.439846ms size : 69784 s=
    write 332.645207ms size : 69784 s=

     */
    private byte[] write(List<Glob> collect, BinWriterFactory writerFactory) {
        long start = System.nanoTime();
        ReusableByteArrayOutputStream outputStream = new ReusableByteArrayOutputStream();
        for (int i = 0; i < 1000; i++) {
            outputStream = new ReusableByteArrayOutputStream();
            GlobBinWriter globBinWriter = writerFactory.create(outputStream);
            globBinWriter.write(collect);
        }
        long end = System.nanoTime();
        System.out.println("write " + (end - start) / 1000000. + "ms size : " + outputStream.size() + " s=" + new String(outputStream.getBuffer(), 0, 10));  // 1100ms puis 600ms
        return outputStream.getBuffer();
    }

    /*
    MSI
    read 267.040952ms => 3744743.989678407 objects/s
    read 265.54786ms => 3765799.505972294 objects/s

LENOVO :
   read 140.288505ms => 7128167.771122802 objects/s
   read 126.594838ms => 7899216.238185004 objects/s

     */
    private void read(BinReader binReader, byte[] s) {
        long start = System.nanoTime();
        Glob[] globs = new Glob[0];
        for (int i = 0; i < 1000; i++) {
            globs = binReader.readArray(s);
            Assert.assertEquals(globs.length, 1000);
        }
        long end = System.nanoTime();
        System.out.println("read " + (end - start) / 1000000. + "ms => " + ((1000. * 1000.) / ((end - start) / 1000000.) * 1000.) + " objects/s");  // 600ms (1.7Millions par second)
        System.out.println(globs[10].toString());
    }
}
