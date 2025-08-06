package org.globsframework.serialisation.stream;

import org.globsframework.core.utils.serialization.SerializedInputOutputFactory;
import org.globsframework.core.utils.serialization.SerializedOutput;
import org.globsframework.serialisation.WireConstants;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class CodedOutputStream {
    private SerializedOutput serializedOutput;

    public CodedOutputStream(SerializedOutput serializedOutput) {
        this.serializedOutput = serializedOutput;
    }

    public static CodedOutputStream newInstance(SerializedOutput serializedOutput) {
        return new CodedOutputStream(serializedOutput);
    }

    public static CodedOutputStream newInstance(OutputStream outputStream) {
        return new CodedOutputStream(SerializedInputOutputFactory.init(outputStream));
    }

    public void writeInt(int value) {
        serializedOutput.write(value);
    }

    public void writeStartGlob() {
        serializedOutput.write(WireConstants.makeTag(0, WireConstants.Type.START_GLOB));
    }

    public void writeEndGlob() {
        serializedOutput.write(WireConstants.makeTag(0, WireConstants.Type.END_GLOB));
    }

    public void writeNull() {
        serializedOutput.write(WireConstants.makeTag(0, WireConstants.Type.NULL));
    }

    public void writeNull(int fieldNumber) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.NULL));
    }

    public void writeBoolean(int fieldNumber, boolean value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.BOOLEAN));
        serializedOutput.write(value);
    }

    public void writeBooleanArray(int fieldNumber, boolean[] value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.BOOLEAN_ARRAY));
        serializedOutput.write(value);
    }

    public void writeInt32(int fieldNumber, int value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.INT));
        serializedOutput.write(value);
    }

    public void writeIntArray(int fieldNumber, int[] value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.INT_ARRAY));
        serializedOutput.write(value);
    }

    public void writeLong(int fieldNumber, long value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.LONG));
        serializedOutput.write(value);
    }

    public void writeLongArray(int fieldNumber, long[] value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.LONG_ARRAY));
        serializedOutput.write(value);
    }

    public void writeDouble(int fieldNumber, double value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.DOUBLE));
        serializedOutput.write(value);
    }

    public void writeDoubleArray(int fieldNumber, double[] value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.DOUBLE_ARRAY));
        serializedOutput.write(value);
    }

    public void writeBigDecimal(int fieldNumber, BigDecimal value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.BIG_DECIMAL));
        serializedOutput.write(new BigDecimal[]{value});
    }

    public void writeBigDecimalArray(int fieldNumber, BigDecimal[] value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.BIG_DECIMAL_ARRAY));
        serializedOutput.write(value);
    }

    public void writeUtf8String(int fieldNumber, String value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.STRING));
        serializedOutput.writeUtf8String(value);
    }

    public void writeStringArray(int fieldNumber, String[] value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.STRING_ARRAY));
        serializedOutput.write(value);
    }

    public void writeLocalDate(int fieldNumber, LocalDate value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.DATE));
        int val = value.getYear() << 9 | value.getMonthValue() << 5 | value.getDayOfMonth();
        serializedOutput.write(val);
    }

    public void writeZonedDateTime(int fieldNumber, ZonedDateTime value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.DATE_TIME));
        //  Month : 4, day : 5,
        int val1 = (value.getYear() & 0xFFFFF) << 9 | value.getMonthValue() << 5 | value.getDayOfMonth();
        serializedOutput.write(val1);
        // hour : 5, minutes : 6, second 6
        int val2 = value.getHour() << 12 | value.getMinute() << 6 | value.getSecond();
        serializedOutput.write(val2);
        serializedOutput.write(value.getNano());
        serializedOutput.writeUtf8String(value.getZone().getId());
    }

    public void writeBytes(int fieldNumber, byte[] value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.BYTES));
        serializedOutput.writeBytes(value);
    }

    public void writeGlob(int fieldNumber) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.GLOB));
    }

    public void writeGlobArray(int fieldNumber, int size) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.GLOB_ARRAY));
        serializedOutput.write(size);
    }

    public void writeGlobUnion(int fieldNumber) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.GLOB_UNION));
    }

    public void writeGlobArrayUnion(int fieldNumber, int size) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.GLOB_UNION_ARRAY));
        serializedOutput.write(size);
    }
}
