package org.github.jrbase.bit;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class BitOperateTest {
    String andy = "abc";
    String me = "我";

    @Test
    public void testMe() {

        byte[] bits = me.getBytes(StandardCharsets.UTF_8);
        Assert.assertEquals(3, bits.length);
        Assert.assertEquals(-26, bits[0]);
        Assert.assertEquals(-120, bits[1]);
        Assert.assertEquals(-111, bits[2]);

    }

    @Test
    public void testSetBit() {

        byte[] bits = andy.getBytes(StandardCharsets.UTF_8);
        Assert.assertEquals(97, bits[0]);
        Assert.assertEquals(98, bits[1]);
        Assert.assertEquals(99, bits[2]);
    }

    @Test
    public void test() {
        Assert.assertEquals(0, (97 & (0x01 >> 5)));
    }


}
