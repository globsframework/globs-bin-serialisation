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

    public static int makeTag(final int fieldNumber, final int wireType) {
        return (fieldNumber << TAG_TYPE_BITS) | wireType;
    }

    public static class Type {
        public static final int NULL = 1;
        public static final int START_GLOB = 2;
        public static final int END_GLOB = 3;
        public static final int BOOLEAN = 4;
        public static final int BOOLEAN_ARRAY = 5;
        public static final int INT = 6;
        public static final int INT_ARRAY = 7;
        public static final int LONG = 8;
        public static final int LONG_ARRAY = 9;
        public static final int DOUBLE = 10;
        public static final int DOUBLE_ARRAY = 11;
        public static final int BIG_DECIMAL = 12;
        public static final int BIG_DECIMAL_ARRAY = 13;
        public static final int STRING = 14;
        public static final int STRING_ARRAY = 15;
        public static final int DATE = 16;
        public static final int DATE_TIME = 17;
        public static final int BYTES = 18;
        public static final int GLOB = 19;
        public static final int GLOB_ARRAY = 21;
        public static final int GLOB_UNION = 22;
        public static final int GLOB_UNION_ARRAY = 23;
    }
}
