package io.github.jrbase.process.hash;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.database.ListRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.handler.CmdHandler;
import io.github.jrbase.process.list.LPopProcess;
import io.github.jrbase.process.list.LPushProcess;
import io.github.jrbase.process.list.RPushProcess;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

public class MutliThreadTest {

    @Test
    public void test8MultiThreadHashSet() throws InterruptedException {
        int threadCount = 10000;
        CmdHandler chandler = CmdHandler.newSingleInstance(null);
        chandler.getDefaultDB().clear();
        CountDownLatch downLatch = new CountDownLatch(threadCount);
        HSetProcess cmdProcess = new HSetProcess();
        for (int i = 0; i < threadCount; i++) {
            int captureI = i;
            new Thread(() -> {
                ClientCmd client = new ClientCmd();
                client.setDb(chandler.getDefaultDB());
                client.setKey("key");
                client.setArgs(new String[]{"" + captureI, "v" + captureI});
                final String process = cmdProcess.process(client);
                downLatch.countDown();
            }).start();
        }
        downLatch.await();

        HLenProcess hLenProcess = new HLenProcess();
        ClientCmd client = new ClientCmd();
        client.setDb(chandler.getDefaultDB());
        client.setKey("key");
        final String process = hLenProcess.process(client);
        assertEquals(":" + threadCount + "\r\n", process);
    }

    @Test
    public void test8MultiThreadLPush() throws InterruptedException {
        int threadCount = 10000;
        CmdHandler chandler = CmdHandler.newSingleInstance(null);
        chandler.getDefaultDB().clear();
        CountDownLatch downLatch = new CountDownLatch(threadCount);
        LPushProcess lPushProcess = new LPushProcess();
        RPushProcess rPushProcess = new RPushProcess();
        for (int i = 0; i < threadCount; i++) {
            int captureI = i;
            new Thread(() -> {
                ClientCmd client = new ClientCmd();
                client.setDb(chandler.getDefaultDB());
                client.setKey("key");
                client.setArgs(new String[]{"" + captureI});
                if (captureI % 2 == 0) {
                    lPushProcess.process(client);
                } else {
                    rPushProcess.process(client);
                }
                downLatch.countDown();
            }).start();
        }
        downLatch.await();

        final RedisValue value = chandler.getDefaultDB().get("key");
        final ListRedisValue listRedisValue = (ListRedisValue) value;
        assertEquals(threadCount, listRedisValue.getSize());
    }

    @Test
    public void test8MultiThreadLPUSHLPop() throws InterruptedException {
        int threadCount = 10000;
        CmdHandler chandler = CmdHandler.newSingleInstance(null);
        chandler.getDefaultDB().clear();
        CountDownLatch downLatch = new CountDownLatch(threadCount);
        LPushProcess lPushProcess = new LPushProcess();
        LPopProcess lPopProcess = new LPopProcess();
        for (int i = 0; i < threadCount; i++) {
            int captureI = i;
            new Thread(() -> {
                ClientCmd client = new ClientCmd();
                client.setDb(chandler.getDefaultDB());
                client.setKey("key");
                client.setArgs(new String[]{"" + captureI});

                lPushProcess.process(client);
                lPopProcess.process(client);

                downLatch.countDown();
            }).start();
        }
        downLatch.await();

        final RedisValue value = chandler.getDefaultDB().get("key");
        final ListRedisValue listRedisValue = (ListRedisValue) value;
        assertEquals(0, listRedisValue.getSize());
    }

}
