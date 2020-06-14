package io.github.jrbase.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RedisRequestCommandTest {


    @Test
    public void testToRedisCommand() {
        String command = RedisRequestCommand.toRedisCommand("set key value");
        assertEquals("*3\r\n$3\r\nset\r\n$3\r\nkey\r\n$5\r\nvalue\r\n", command);

        command = RedisRequestCommand.toRedisCommand(" set  key   value ");
        assertEquals("*3\r\n$3\r\nset\r\n$3\r\nkey\r\n$5\r\nvalue\r\n", command);
    }

    @Test
    public void testGetItems() {
        String[] command = RedisRequestCommand.getItems("set a b");
        assertEquals("set", command[0]);
        assertEquals("a", command[1]);
        assertEquals("b", command[2]);

        command = RedisRequestCommand.getItems(" set  a  b  ");
        assertEquals("set", command[0]);
        assertEquals("a", command[1]);
        assertEquals("b", command[2]);
    }


}
