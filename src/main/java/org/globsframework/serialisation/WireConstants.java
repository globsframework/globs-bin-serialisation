package org.globsframework.serialisation;

public class WireConstants {
    static final int TAG_TYPE_BITS = 5;
    static final int TAG_TYPE_MASK = (1 << TAG_TYPE_BITS) - 1;

    public static int getTagWireType(final int tag) {
        return tag & TAG_TYPE_MASK;
    }

    public static int getTagFieldNumber(final int tag) {
        return tag >>> TAG_TYPE_BITS;
    }

    static int makeTag(final int fieldNumber, final int wireType) {
        return (fieldNumber << TAG_TYPE_BITS) | wireType;
    }

    static class Type {
        static final int NULL = 1;
        static final int START_GLOB = 2;
        static final int END_GLOB = 3;
        static final int BOOLEAN = 4;
        static final int BOOLEAN_ARRAY = 5;
        static final int INT = 6;
        static final int INT_ARRAY = 7;
        static final int LONG = 8;
        static final int LONG_ARRAY = 9;
        static final int DOUBLE = 10;
        static final int DOUBLE_ARRAY = 11;
        static final int BIG_DECIMAL = 12;
        static final int BIG_DECIMAL_ARRAY = 13;
        static final int STRING = 14;
        static final int STRING_ARRAY = 15;
        static final int DATE = 16;
        static final int DATE_TIME = 17;
        static final int BYTES = 18;
        static final int GLOB = 19;
        static final int GLOB_ARRAY = 21;
        static final int GLOB_UNION = 22;
        static final int GLOB_UNION_ARRAY = 23;
    }

}
