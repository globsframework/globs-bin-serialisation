package org.globsframework.serialisation.stream;

import org.globsframework.serialisation.WireConstants;
import org.globsframework.utils.serialization.SerializedInputOutputFactory;
import org.globsframework.utils.serialization.SerializedOutput;

import java.io.OutputStream;

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

    public void writeInt32(int fieldNumber, int value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.INT));
        serializedOutput.write(value);
    }

    public void writeLong(int fieldNumber, long value) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.LONG));
        serializedOutput.write(value);
    }

    public void writeGlob(int fieldNumber) {
        serializedOutput.write(WireConstants.makeTag(fieldNumber, WireConstants.Type.GLOB));
    }
}
