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

    public void writeNull(int index) {
        serializedOutput.write(WireConstants.makeTag(index, WireConstants.Type.NULL));
    }

    public void writeInt32(int index, int value) {
        serializedOutput.write(WireConstants.makeTag(index, WireConstants.Type.INT));
        serializedOutput.write(value);
    }

    public void writeGlob(int index) {
        serializedOutput.write(WireConstants.makeTag(index, WireConstants.Type.GLOB));
    }
}
