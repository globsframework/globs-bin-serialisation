package org.globsframework.serialisation;

import org.junit.Assert;
import org.junit.Test;

public class DateTimeEncodeTest {
    @Test
    public void encodeDate() {
        checkDate(20090, 12, 31); // 24 bits
        checkDate(2023, 1, 1);
    }

    private static void checkDate(int y, int m, int d) {
        int val = (y & 0xFFFFF) << 9 | m << 5 | d;
        int year = (val >>> 9);
        int month = (val >>> 5) & 0xF;
        int dayOfMonth = ((int) (val & 0x1F));
        Assert.assertEquals(y, year);
        Assert.assertEquals(m, month);
        Assert.assertEquals(d, dayOfMonth);
    }

    @Test
    public void encodeDateTime() {
        checkTime(24, 59, 59); // 17 bits
        checkTime(1, 9, 38);
        checkTime(0, 0, 0);
    }

    private static void checkTime(int h, int m, int s) {
        int val = h << 12 | m << 6 | s;
        int hour = (val >>> 12);
        int minute = (val >>> 6) & 0x3F;
        int second = ((int) (val & 0x3F));
        Assert.assertEquals(h, hour);
        Assert.assertEquals(m, minute);
        Assert.assertEquals(s, second);
    }
}
