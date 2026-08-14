package org.globsframework.serialisation.stream;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.GlobInstantiator;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.core.utils.serialization.SerializedInput;
import org.globsframework.core.utils.serialization.SerializedInputOutputFactory;
import org.globsframework.core.model.generate.write.CallAtWrite;
import org.globsframework.core.model.generate.write.GeneratedCallerWrite;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.manager.GlobTypeFieldReadersManager;
import org.jspecify.annotations.Nullable;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.globsframework.serialisation.WireConstants.Type.END_GLOB;

public final class CodedInputStream implements CallAtWrite {
    /** what getNextToCall answers at the end of a glob; field numbers are never negative */
    public static final int END_OF_GLOB = -1;

    private final GlobInstantiator globInstantiator;
    private final GlobTypeFieldReadersManager globTypeFieldReadersManager;
    private final SerializedInput serializedInput;
    // the tag getNextToCall has just read, which the FieldReader it names then reads back rather than being
    // handed it : MutableFunctionWrite takes objects, and an int argument would have to be boxed
    private int lastTag;

    public CodedInputStream(GlobInstantiator globInstantiator, GlobTypeFieldReadersManager globTypeFieldReadersManager, SerializedInput serializedInput) {
        this.globInstantiator = globInstantiator;
        this.globTypeFieldReadersManager = globTypeFieldReadersManager;
        this.serializedInput = serializedInput;
    }

    public static CodedInputStream newInstance(GlobInstantiator globInstantiator, GlobTypeFieldReadersManager globTypeFieldReadersManager, SerializedInput serializedInput) {
        return new CodedInputStream(globInstantiator, globTypeFieldReadersManager, serializedInput);
    }

    public static CodedInputStream newInstance(GlobTypeFieldReadersManager globTypeFieldReadersManager, SerializedInput serializedInput) {
        return new CodedInputStream(GlobType::instantiate, globTypeFieldReadersManager, serializedInput);
    }

    public static CodedInputStream newInstance(GlobTypeFieldReadersManager globTypeFieldReadersManager, InputStream inputStream) {
        return new CodedInputStream(GlobType::instantiate, globTypeFieldReadersManager, SerializedInputOutputFactory.init(inputStream));
    }

    public static CodedInputStream newInstance(GlobTypeFieldReadersManager globTypeFieldReadersManager, byte[] data) {
        return new CodedInputStream(GlobType::instantiate, globTypeFieldReadersManager, SerializedInputOutputFactory.init(data));
    }

    public int readTag() {
        return readInt();
    }

    /**
     * The CallAtWrite of the read loop : the next field number on the wire, or END_OF_GLOB. The tag it comes
     * from is kept for the reader that is about to run — the nested case is safe because a reader reads it
     * back before descending into a sub-glob, which is what overwrites it.
     */
    public int getNextToCall() {
        int tag = readTag();
        lastTag = tag;
        return WireConstants.getTagWireType(tag) == END_GLOB ? END_OF_GLOB : WireConstants.getTagFieldNumber(tag);
    }

    public int lastTag() {
        return lastTag;
    }

    public int lastWireType() {
        return WireConstants.getTagWireType(lastTag);
    }

    public void skipFieldFromWireType(int type) {
        switch (type) {
            case WireConstants.Type.NULL:
                break;
            case WireConstants.Type.BOOLEAN:
                readBoolean();
                break;
            case WireConstants.Type.BOOLEAN_ARRAY:
                readBooleanArray();
                break;
            case WireConstants.Type.INT:
                readInt();
                break;
            case WireConstants.Type.INT_ARRAY:
                readIntArray();
                break;
            case WireConstants.Type.LONG:
                readLong();
                break;
            case WireConstants.Type.LONG_ARRAY:
                readLongArray();
                break;
            case WireConstants.Type.DOUBLE:
                readDouble();
                break;
            case WireConstants.Type.DOUBLE_ARRAY:
                readDoubleArray();
                break;
            case WireConstants.Type.BIG_DECIMAL:
                readBigDecimal();
                break;
            case WireConstants.Type.BIG_DECIMAL_ARRAY:
                readBigDecimalArray();
                break;
            case WireConstants.Type.STRING:
                readUtf8String();
                break;
            case WireConstants.Type.START_GLOB:
                break;
            case WireConstants.Type.STRING_ARRAY:
                readStringArray();
                break;
            case WireConstants.Type.DATE:
                readLocalDate();
                break;
            case WireConstants.Type.DATE_TIME:
                readZonedDateTime();
                break;
            case WireConstants.Type.BYTES:
                readBytes();
                break;
            case WireConstants.Type.GLOB:
                skipGlobField();
                break;
            case WireConstants.Type.GLOB_UNION:
                if (readInt() != -1) {
                    skipGlobField();
                }
                break;
            case WireConstants.Type.GLOB_ARRAY: {
                int size = readInt();
                for (int index = 0; index < size; index++) {
                    skipGlobField();
                }
                break;
            }
            case WireConstants.Type.GLOB_UNION_ARRAY: {
                int size = readInt();
                for (int index = 0; index < size; index++) {
                    if (readInt() != -1) {
                        skipGlobField();
                    }
                }
                break;
            }
            default:
                throw new RuntimeException("type " + type + " not managed yet.");
        }
    }

    private void skipGlobField() {
        while (true) {
            int subTag = readTag();
            int type = WireConstants.getTagWireType(subTag);
            if (type == WireConstants.Type.END_GLOB) {
                break;
            }
            skipFieldFromWireType(type);
        }
    }

    public boolean readBoolean() {
        return serializedInput.readBoolean();
    }

    public boolean[] readBooleanArray() {
        return serializedInput.readBooleanArray();
    }

    public int readInt() {
        return serializedInput.readNotNullInt();
    }

    public int readByte() {
        return serializedInput.readByte();
    }

    public int[] readIntArray() {
        return serializedInput.readIntArray();
    }

    public long readLong() {
        return serializedInput.readNotNullLong();
    }

    public long[] readLongArray() {
        return serializedInput.readLongArray();
    }

    public double readDouble() {
        return serializedInput.readNotNullDouble();
    }

    public double[] readDoubleArray() {
        return serializedInput.readDoubleArray();
    }

    public BigDecimal readBigDecimal() {
        return serializedInput.readBigDecimal();
    }

    public BigDecimal[] readBigDecimalArray() {
        return serializedInput.readBigDecimalArray();
    }

    public String readUtf8String() {
        return serializedInput.readUtf8String();
    }

    public String[] readStringArray() {
        return serializedInput.readStringArray();
    }

    public LocalDate readLocalDate() {
        int all = readInt();
        int year = (all >>> 9);
        int month = (all >>> 5) & 0xF;
        int dayOfMonth = ((int) (all & 0x1F));

        return LocalDate.of(year, month, dayOfMonth);
    }

    public ZonedDateTime readZonedDateTime() {
        int val1 = readInt();
        int year = (val1 >>> 9);
        int month = (val1 >>> 5) & 0xF;
        int dayOfMonth = ((int) (val1 & 0x1F));
        int val2 = readInt();
        int hour = (val2 >>> 12);
        int minute = (val2 >>> 6) & 0x3F;
        int second = ((int) (val2 & 0x3F));
        int nanoOfSecond = readInt();
        ZoneId zoneId = ZoneId.of(readUtf8String());
        return ZonedDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, zoneId);
    }

    public byte[] readBytes() {
        return serializedInput.readBytes();
    }

    public @Nullable Glob readGlob(@Nullable GlobType globType, @Nullable GlobTypeFieldReaders fieldReaders) {
        int tag = readTag();
        final int wireType = WireConstants.getTagWireType(tag);
        if (wireType == WireConstants.Type.NULL) {
            return null;
        }
        if (wireType != WireConstants.Type.START_GLOB) {
            throw new RuntimeException("Expecting Glob but got " + tag + " : " + wireType);
        }

        if (globType != null && fieldReaders != null) {
            return _readGlob(globType, fieldReaders);
        } else {
            while (true) {
                int fieldTag = readTag();
                int tagWireType = WireConstants.getTagWireType(fieldTag);

                if (tagWireType == END_GLOB) {
                    return null;
                }
                skipFieldFromWireType(tagWireType);
            }
        }
    }

    private Glob _readGlob(GlobType globType, GlobTypeFieldReaders globTypeFieldReaders) {
        MutableGlob data = globInstantiator.newGlob(globType);
        // one test per glob, not per field : with a caller the whole loop is the generated switch, without
        // one it is the array below, which is what this reads when -Dglobs.callerWrite is unset
        GeneratedCallerWrite<CodedInputStream, Void, Void> caller = globTypeFieldReaders.caller();
        if (caller != null) {
            caller.call(this, data, this, null, null);
            return data;
        }
        while (true) {
            int fieldTag = readTag();
            int fieldNumber = WireConstants.getTagFieldNumber(fieldTag);
            int tagWireType = WireConstants.getTagWireType(fieldTag);

            if (tagWireType == END_GLOB) {
                return data;
            }

            globTypeFieldReaders.get(fieldNumber)
                    .read(data, fieldTag, tagWireType, this);
        }
    }
}
