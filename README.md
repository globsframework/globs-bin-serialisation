This library serializes Globs in a binary format that is backward compatible.
It is a kind of TLV (Type Length Value) a la Google Protocol Buffer.
The default binary serialization provided in Globs is more efficient but is not backward compatible, as the values are
directly written in the order of the fields in the GlobType.

## Declaring a type

An id is given to each field with `FieldNumber`. Fields without a field number are neither written nor read.

```java
public class Person {
    public static final GlobType TYPE;

    public static final StringField name;
    public static final IntegerField age;
    public static final GlobArrayField addresses;

    static {
        GlobTypeBuilder builder = GlobTypeBuilderFactory.create("Person");
        name = builder.declareStringField("name", FieldNumber.create(1));
        age = builder.declareIntegerField("age", FieldNumber.create(2));
        addresses = builder.declareGlobArrayField("addresses", () -> Address.TYPE, FieldNumber.create(3));
        TYPE = builder.build();
    }
}
```

Union fields also need a mapping from each target type to a stable wire index:

```java
    owner = builder.declareGlobUnionField("owner",
            new Supplier[]{() -> Address.TYPE, () -> Company.TYPE},
            FieldNumber.create(4),
            UnionType.create(UnionType.ChoiceType.create("Address", 1),
                             UnionType.ChoiceType.create("Company", 2)));
```

The `@FieldNumber_(n)` and `@UnionType_({...})` Java annotations carry the same information for types built from
annotated classes.

## Writing and reading

```java
Glob person = Person.TYPE.instantiate()
        .set(Person.name, "Marc")
        .set(Person.age, 42);

ByteArrayOutputStream out = new ByteArrayOutputStream();
BinWriter binWriter = BinWriterFactory.create().createFromStream(out);
binWriter.write(person);

BinReader binReader = BinReaderFactory.create()
        .createFromStream(new ByteArrayInputStream(out.toByteArray()));
Glob read = binReader.read(Person.TYPE);

assertEquals("Marc", read.get(Person.name));
```

The GlobType is not written to the stream: the reader is given the type to read. A field that is present in the
stream but absent from that type is skipped, which is what makes the format backward and forward compatible.

`write(Collection<Glob>)` / `readArray(GlobType)` handle sequences of globs. Nulls are preserved: a field explicitly
set to null is read back as an explicit null, while a field that was never set stays unset.

## Reusing readers and writers

The reader and writer for a GlobType are computed once and cached. When several globs of the same type are written to
the same stream, `getWriter` / `getReader` avoid the per-call type lookup, and the managers can be built upfront so
that no type is analysed on the hot path:

```java
GlobTypeFieldWritersManager writers = GlobTypeFieldWritersManager.Builder.init()
        .add(Person.TYPE)
        .add(Address.TYPE)
        .build();

BinWriter binWriter = BinWriterFactory.create(writers).createFromStream(out);
BinWriter.GlobWriter globWriter = binWriter.getWriter(Address.TYPE);
globWriter.write(List.of(paris, lyon));
```

`GlobTypeFieldReadersManager.Builder` does the same for reading, and `BinReader.getReader(type).readArray()` reads the
globs back.

Both factories also accept the Globs serialization streams instead of `java.io` ones:
`binWriterFactory.create(SerializedOutput)` and `binReaderFactory.createFromStream(SerializedInput)`.
