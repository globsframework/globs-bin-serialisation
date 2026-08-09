# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

`org.globsframework:globs-bin-serialisation` — binary serialisation of `Glob` objects in a TLV (Type-Length-Value) format inspired by Protocol Buffers. Unlike the default serialisation shipped in the `globs` core library (which writes values in field-declaration order and is therefore *not* backward compatible), this format tags every value with a field number so readers can skip unknown fields and tolerate schema evolution.

Single Maven module, Java 21 (`maven-compiler-plugin` `source`/`target` 21 — note the GitHub workflows still set up JDK 17, so CI and the POM disagree). Depends on `org.globsframework:globs` (the core Glob metamodel, **5.11-SNAPSHOT** — the writers need `model/generate/`, so this module now tracks a core snapshot: `mvn install` in `globsframework/` before building it) and `jspecify`.

## Commands

```bash
mvn test                            # run all tests
mvn -o -B -Dtest=BinReaderTest test  # single test class (-o works, deps are in ~/.m2)
mvn -o -B -Dtest=BinReaderTest#testBoolean test
mvn package                         # build the jar
mvn -s settings.xml -B package      # what CI runs (settings.xml pulls the GitHub Packages repo)
```

Tests are a mix of JUnit 3 (`BinReaderTest extends TestCase`) and JUnit 4 (`PerfReadWriteTest` with `@Test`); surefire auto-detects the JUnit 4 provider and runs both. `PerfReadWriteTest` is a benchmark that runs as part of `mvn test` and prints throughput to stdout.

Releases go through `maven-release-plugin` + the `release` profile (GPG signing, sources/javadoc, Sonatype Central). `pom.xml.releaseBackup` and `release.properties` in the working tree are leftovers of an interrupted/complete release run, not source files.

## Architecture

### Field numbers drive everything

Each field that should be serialised carries a field number, either via the `@FieldNumber_(n)` annotation on the static field, or by passing `FieldNumber.create(n)` to the `GlobTypeBuilder.declareXxxField(...)` call (the tests use the latter). `FieldNumber` is the Glob-side representation of the `FieldNumber_` Java annotation, wired up with `GlobCreateFromAnnotation`. **A field with no field number is silently not written and not read** — both factories skip fields whose `FieldNumber` annotation is absent.

Unions (`GlobUnionField`, `GlobArrayUnionField`) additionally need `@UnionType_({@ChoiceType_(value = X.class, index = i), ...})` (or `UnionType.create(ChoiceType.create(name, i), ...)`) to map each concrete target type to a stable wire index. Type identity on the wire is that index, resolved back to a `GlobType` by *name* at reader/writer construction time.

### Wire format

`WireConstants` defines the tag encoding: `tag = (fieldNumber << 5) | wireType`, with wire types in `WireConstants.Type` (NULL, START_GLOB, END_GLOB, and one per Glob field kind). A glob is `START_GLOB`, then one tag+value per set field, then `END_GLOB` — there is no length prefix, so skipping a nested glob means scanning to its matching `END_GLOB` (`CodedInputStream.skipGlobField`). Actual byte-level encoding is delegated to the core library's `SerializedInput`/`SerializedOutput` (`SerializedInputOutputFactory`), so `CodedInputStream`/`CodedOutputStream` only deal with tags and the date/datetime bit-packing (`LocalDate` packed into one int; `ZonedDateTime` into two ints + nanos + zone id string).

Backward compatibility rests on two behaviours: an unknown field number resolves to `UnknownFieldReader`, which calls `skipField(tag)`; and a known field number arriving with an unexpected wire type logs an error and skips rather than throwing.

### Per-type reader/writer plans

The central idea is that serialisation logic for a `GlobType` is computed **once** and cached, not re-derived per glob:

- `GlobTypeFieldWriters` holds a `FieldWriter[]` indexed by *field index* (dense, one entry per field of the type; fields without a field number get `NullFieldWriter`). Writing iterates the array in order — unless the type's factory can do better, see below.
- `GlobTypeFieldReaders` holds a `FieldReader[]` indexed by *field number* (sized to the largest field number, gaps filled with `UnknownFieldReader`). Reading looks up by the number decoded from the tag.

The three-layer structure repeats symmetrically for readers and writers:

- `*Factory` (`DefaultGlobTypeFieldReadersFactory` / `...WritersFactory`) builds the plan for one `GlobType`. It inserts the (still empty) container into the shared `containers` map *before* visiting fields, which is what makes recursive/self-referencing types work — `Proto1.globField` targeting `Proto1` resolves to the in-progress instance instead of recursing forever. Preserve that ordering when editing.
- `*Manager` (`DefaultGlobTypeFieldReadersManager` / `...WritersManager`) is the cache lookup (`getOrCreate`). `Builder.init().add(type)...build()` pre-registers types so no plan is built on the hot path.
- `BinReaderFactory` / `BinWriterFactory` are the entry points; `create()` builds a default manager, `create(manager)` takes a pre-built one.

`FieldWriterVisitorCreator` / `FieldReaderVisitorCreator` are `FieldVisitor`s over the core metamodel that instantiate the right `field/writer/*` or `field/reader/*` class per field kind. **Adding support for a new Glob field type means touching four places**: the visitor creator, the concrete reader, the concrete writer, and a new `WireConstants.Type` constant plus its `skipField` case.

### Writing through a generated caller

The `FieldWriter[]` loop is one call site for every `FieldWriter` class in the process, and each writer's `getAccessor.get(data)` is another: both megamorphic, neither inlined. Core's `model/generate/` SPI exists to remove exactly that, so `FieldWriter` **extends `FieldValueFunction<Object, CodedOutputStream, Void>`** — every writer has a second entry point, `call(isSet, isNull, value, out, null)`, handed the value instead of fetching it.

`GlobTypeFieldWriters.initCaller(type)` then asks the type's factory (`type.getGlobFactory() instanceof GlobGenerateFactory`) for a `GeneratedFunctionCaller` over those same writers. With `globs-generate` installed that caller is a generated class holding each writer in a `static final` field, so the write of a field becomes a monomorphic, inlinable call. Without it, nothing is asked and the loop is unchanged — `GenerateCaller.callerFor` is deliberately **not** used, because its `DefaultFunctionCaller` fallback reads through `Glob.getValue` rather than the typed accessor each writer holds, and calls the writers of fields that have no field number, which `NullFieldWriter` makes free in the loop.

Three things to respect:

- **`call` and `write` must produce the same bytes.** Only null-vs-unset drives the choice between writing nothing, a NULL tag and the value, and it is the same test on both paths (`isNull` from the caller means "`getValue` answers null"). `GeneratedCallerWriterTest` writes the same data through both and compares the bytes; break one `call` and both of its tests fail.
- **`initCaller` runs at the end of `DefaultGlobTypeFieldWritersFactory.create`**, not in the constructor: the factory publishes the (empty) `GlobTypeFieldWriters` into `containers` before visiting the fields so recursive types resolve, so the `FieldWriter[]` is only complete at that point.
- **the caller is guarded by `glob.getClass() == generatedGlobClass`** (captured from `type.instantiate()`). A generated caller reads the fields of its own Glob class directly, so a `MutableGlob` from a custom `GlobInstantiator` has to take the loop rather than a `ClassCastException`. One reference compare per glob, and `null` when there is no caller, which is why there is no second test.

Measured end to end (200k globs of 4 / 20 / 40 fields, write only, `globs-generate` object flavour), caller off → on: **16.9 → 19.8**, **2.81 → 4.46**, **1.15 → 2.20 M globs/s** (+17 % / +59 % / +91 %). Note the baseline that matters: with generated globs and *no* caller, 40 fields writes at 1.15 M globs/s against **1.95** for core's plain `DefaultGlob` — generation alone makes this module slower, because one accessor class per field is more receivers at the same megamorphic call site. The caller is what makes generation pay here.

### Reading and writing

```java
BinWriter w = BinWriterFactory.create().createFromStream(out);
w.write(glob);                          // or write(Collection<Glob>)
BinWriter.GlobWriter gw = w.getWriter(Proto1.TYPE);  // avoids the per-call type lookup

BinReader r = BinReaderFactory.create().createFromStream(in);
Glob g = r.read(Proto1.TYPE);           // the type is supplied by the caller, not stored on the wire
```

The `GlobType` is **not** written to the stream — the reader must be told which type to expect. Reading with `type == null` consumes and discards a glob. `BinReaderFactory.create(globInstantiator, ...)` lets callers override how `MutableGlob` instances are allocated.

Note: the README's example is out of date (it shows `create(outputStream)` and `createGlobBinReader(GlobTypeResolver...)`); the current API is `createFromStream(...)` and `read(GlobType)`. `BinReaderTest` is the authoritative usage reference — it round-trips every field kind, including reading into a type where the field is absent (forward/backward compatibility) and explicit-null handling.

### Null vs unset

The distinction matters and is tested. A field explicitly set to `null` is written as a `NULL`-tagged entry (`getAccessor.isSet(data)` guards this in the writers) and restored as an explicit null on read; a field never set writes nothing at all and stays unset after a round-trip.
