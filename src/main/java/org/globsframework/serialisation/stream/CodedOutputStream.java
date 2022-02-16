package org.globsframework.serialisation.stream;

import org.globsframework.serialisation.WireConstants;
import org.globsframework.utils.serialization.SerializedInputOutputFactory;
import org.globsframework.utils.serialization.SerializedOutput;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class CodedOutputStream {
    private SerializedOutput serializedOutput;

    public CodedOutputStream(SerializedOutput serializedOutput) {
        this.serializedOutput = serializedOutput;
    }

    public static CodedOutputStream newInstance(OutputStream outputStream) {
        return new CodedOutputStream(SerializedInputOutputFactory.init(outputStream));
    }

    public void writeStartGlob(String typeName) {
        serializedOutput.write(WireConstants.makeTag(0, WireConstants.Type.START_GLOB));
        serializedOutput.writeUtf8String(typeName);
    }

    public void writeEndGlob() {
        serializedOutput.write(WireConstants.makeTag(0, WireConstants.Type.END_GLOB));
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
        serializedOutput.write(new BigDecimal[] {value});
    }

    public void writeBigDecimalArray(int fieldNumber, BigDecimal[] value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.BIG_DECIMAL_ARRAY));
        serializedOutput.write(value);
    }

    public void writeUtf8String(int fieldNumber, String value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.STRING));
        serializedOutput.writeUtf8String(value);
    }

    public void writeLocalDate(int fieldNumber, LocalDate value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.DATE));
        serializedOutput.write(value.getYear());
        serializedOutput.write(value.getMonthValue());
        serializedOutput.write(value.getDayOfMonth());
    }

    public void writeZonedDateTime(int fieldNumber, ZonedDateTime value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.DATE_TIME));
        serializedOutput.write(value.getYear());
        serializedOutput.write(value.getMonthValue());
        serializedOutput.write(value.getDayOfMonth());
        serializedOutput.write(value.getHour());
        serializedOutput.write(value.getMinute());
        serializedOutput.write(value.getSecond());
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
}
