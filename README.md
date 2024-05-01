This library is useful to serialized Glob in a binary format that is backward compatible.
It is a kind of TLV (Type Length Value) a la Google Protocol Buffer.
The default binary serialisation privided in Globs is more efficient but is not backward compatible as the value are directly written in the order of the fields in the GlobType.

An id is given to each field using annotation FieldNumber_.

```
       Glob p = Proto1.TYPE.instantiate()
                .set(Proto1.booleanField, true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GlobBinWriter binWriter = BinWriterFactory.create().create(byteArrayOutputStream);
        binWriter.write(p);

        GlobType readType = ex.getType();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        GlobBinReader binReader = BinReaderFactory.create().createGlobBinReader(GlobTypeResolver.from(readType));
        Glob r = binReader.read(inputStream).get();
        
        assertTrue(r.isTrue(Proto1.booleanField));

```
