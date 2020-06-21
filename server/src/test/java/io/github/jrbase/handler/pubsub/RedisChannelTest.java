package io.github.jrbase.handler.pubsub;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RedisChannelTest {

    /**
     * h?llo subscribes to hello, hallo and hxllo
     * h*llo subscribes to hllo and heeeello
     * h[ae]llo subscribes to hello and hallo, but not hillo
     */
    @Test
    public void testPatten1() {
        String patten = "h?llo";
        Pattern compile = Pattern.compile(patten);
        Matcher m1;
        m1 = compile.matcher("hello");
        assertTrue(m1.find());

        m1 = compile.matcher("hallo");
        assertTrue(m1.find());

        m1 = compile.matcher("hxllo");
        assertTrue(m1.find());
    }

    @Test
    public void testPatten2() {
        Pattern compile = Pattern.compile("h*llo");

        Matcher m1 = compile.matcher("hllo");
        assertTrue(m1.find());

        m1 = compile.matcher("heeeello");
        assertTrue(m1.find());

        m1 = compile.matcher("hxllo");
        assertTrue(m1.find());
    }

    @Test
    public void testPatten3() {
        Pattern compile = Pattern.compile("h[ae]llo");

        Matcher m1 = compile.matcher("hello");
        assertTrue(m1.find());

        m1 = compile.matcher("hallo");
        assertTrue(m1.find());

        m1 = compile.matcher("hillo");
        assertFalse(m1.find());
    }
}
