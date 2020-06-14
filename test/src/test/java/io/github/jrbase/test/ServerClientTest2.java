package io.github.jrbase.test;

import io.github.jrbase.JRServerEmbedded;
import io.github.jrbase.client.RedisClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ServerClientTest2 {

    private static final JRServerEmbedded jrServerEmbedded = new JRServerEmbedded("6379");

    @BeforeClass
    public static void before() throws IOException, InterruptedException {
        jrServerEmbedded.start();
        Thread.sleep(1000);
    }

    @AfterClass
    public static void after() {
        jrServerEmbedded.stop();
    }

    @Test
    public void testReceiveTimeout() {
        RedisClient redisClient = new RedisClient();
        redisClient.connect();

        redisClient.sendMessage("ping 123");
        redisClient.stop();
        String message = redisClient.receiveMsg(2000, TimeUnit.MILLISECONDS);

        Assert.assertNull(message);

    }
}
