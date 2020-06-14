package io.github.jrbase.test;

import io.github.jrbase.JRServerEmbedded;
import io.github.jrbase.client.RedisClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerClientTest {

    private static ExecutorService executorService;
    private static final JRServerEmbedded jrServerEmbedded = new JRServerEmbedded("6379");

    @BeforeClass
    public static void before() throws IOException, InterruptedException {
        jrServerEmbedded.start();
        Thread.sleep(1000);
        executorService = Executors.newFixedThreadPool(2);
    }

    @AfterClass
    public static void end() {
        jrServerEmbedded.stop();
    }

    @Test
    public void testSmock() {
        RedisClient redisClient = new RedisClient();
        redisClient.connect();

        final String message = redisClient.sendMessageAndReceive("ping 123");

        System.out.println(message);
        Assert.assertEquals("$3\r\n123\r\n", message);

        final String message2 = redisClient.sendMessageAndReceive("ping");
        System.out.println(message2);
        Assert.assertEquals("+PONG\r\n", message2);

        redisClient.stop();

    }

    @Test
    public void testReceiveTimeout() {
        RedisClient redisClient = new RedisClient();
        redisClient.connect();

        redisClient.sendMessage("ping 123");
        String message = redisClient.receiveMsg(2000, TimeUnit.MILLISECONDS);

        Assert.assertEquals("$3\r\n123\r\n", message);

    }

    @Test
    public void testSubscribe() {
        RedisClient redisClient = new RedisClient();
        redisClient.connect();

        final String actual = redisClient.sendMessageAndReceive("subscribe channel1 channel2");
        System.out.println(actual);
        StringBuilder expected = new StringBuilder();
        List<String> channels = new ArrayList<>(Arrays.asList("channel1", "channel2"));
        for (int i = 0; i < channels.size(); i++) {
            final String ch = channels.get(i);
            expected.append("*").append(3).append("\r\n")
                    .append("$").append(9).append("\r\n").append("subscribe").append("\r\n")
                    .append("$").append(ch.length()).append("\r\n").append(ch).append("\r\n")
                    .append(":").append(i + 1).append("\r\n");
        }
        redisClient.stop();
        Assert.assertEquals(expected.toString(), actual);

    }

    @Test
    public void testPublishMessage() throws InterruptedException {
        RedisClient redisClient = new RedisClient();
        redisClient.connect();

        String some = redisClient.sendMessageAndReceive("subscribe channel1 channel2");
        System.out.println(some + "||");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        executorService.submit(() -> {
            final RedisClient publisher = new RedisClient();
            publisher.connect();
            String result = publisher.sendMessageAndReceive("publish channel1 mes112233");
            System.out.println(result);

            publisher.stop();
            countDownLatch.countDown();
        });

        countDownLatch.await();
        String receiveMessage = redisClient.receiveMsg();
        redisClient.stop();

        Assert.assertEquals("*3\r\n$7\r\nmessage\r\n$8\r\nchannel1\r\n$9\r\nmes112233\r\n", receiveMessage);

    }


}
