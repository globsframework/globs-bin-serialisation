package org.globsframework.serialisation.stream;

import org.globsframework.serialisation.WireConstants;
import org.globsframework.utils.serialization.SerializedInput;
import org.globsframework.utils.serialization.SerializedInputOutputFactory;

import java.io.InputStream;

public class CodedInputStream {
    private SerializedInput serializedInput;

    public CodedInputStream(SerializedInput serializedInput) {
        this.serializedInput = serializedInput;
    }

    public static CodedInputStream newInstance(InputStream inputStream) {
        return new CodedInputStream(SerializedInputOutputFactory.init(inputStream));
    }

    public int readTag() {
        return serializedInput.readNotNullInt();
    }

    public String readUtf8String() {
        return serializedInput.readUtf8String();
    }

    public void skipField(int tag) {
        int type = WireConstants.getTagWireType(tag);
        switch (type) {
            case WireConstants.Type.NULL:
                break;
            case WireConstants.Type.INT:
                serializedInput.readNotNullInt();
                break;
            case WireConstants.Type.GLOB:
                skipGlobField(tag);
                break;
            case WireConstants.Type.START_GLOB:
                serializedInput.readUtf8String();
                break;
            default:
                throw new RuntimeException("type " + type + " not managed yet.");
        }
    }

    private void skipGlobField(int tag) {
        while (true) {
            int subTag = readTag();
            if (WireConstants.getTagWireType(subTag) == WireConstants.Type.END_GLOB) {
                break;
            }
            skipField(subTag);
        }
    }

    public int readInt() {
        return serializedInput.readNotNullInt();
    }
}
