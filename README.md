# Globs Binary Serialisation

Serialize [Glob](https://globsframework.org)s in a **backward-compatible binary format** — a TLV
(Type Length Value) shape in the spirit of Google Protocol Buffers. Each field carries a stable number, so a
reader built from an older version of the type skips what it does not know and keeps working.

The default binary serialization shipped in Globs core is faster but *not* backward compatible: it writes the
values in field-declaration order, so both ends must share the exact same `GlobType`.

## Requirements

Java 21, `org.globsframework:globs`.

## Installation

```xml
<dependency>
    <groupId>org.globsframework</groupId>
    <artifactId>globs-bin-serialisation</artifactId>
    <version>5.4.0</version>
</dependency>
```

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


## Performance

Reading and writing go through a **caller** from core's `model/caller` SPI when one can be generated: the
per-field writers are held in `static final` fields of a generated class, so each call site is monomorphic
and inlines, instead of one megamorphic site shared by every writer class in the process. Put
[globs-generate](https://github.com/globsframework/globs-generate) on the classpath and switch it on:

```
-Dglobs.caller.fromGlob=org.globsframework.model.generator.AsmCallerGeneratorService
-Dglobs.caller.toGlob=org.globsframework.model.generator.AsmCallerWriteGeneratorService
```

Measured on 200 000 globs of 4 / 20 / 40 fields, caller off → on: **16.9 → 19.8**, **2.81 → 4.46**,
**1.15 → 2.20 M globs/s**. It pays on core's plain `DefaultGlob` too (+18 % on write, +23 % on a nested
shape), which is the point: the gain is the removed dispatch, not the generated Glob class — generating the
Glob classes *alone* actually makes this module slower.

Whichever path a JVM takes, the bytes are identical; `GeneratedCallerWriterTest` holds the two together.

## Building

```bash
mvn -o test
```

`CLAUDE.md` documents the wire format, the per-type reader/writer plans and the benchmark invocations.

## License

Apache License 2.0 — see <https://www.apache.org/licenses/LICENSE-2.0.txt>.

## Links

- [Globs Framework](https://globsframework.org)
- [GitHub repository](https://github.com/globsframework/globs-bin-serialisation)
