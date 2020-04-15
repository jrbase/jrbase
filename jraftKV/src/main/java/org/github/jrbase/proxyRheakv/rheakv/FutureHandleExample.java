package org.github.jrbase.proxyRheakv.rheakv;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;

/**
 * @author jiachun.fjc
 */
public class FutureHandleExample {

    private static final Logger LOG = LoggerFactory.getLogger(FutureHandleExample.class);

    public static void main(final String[] args) throws InterruptedException {
        final Client client = new Client();
        client.init();
        get(client.getRheaKVStore());
        client.shutdown();
    }

    public static void get(final RheaKVStore backendProxy) throws InterruptedException {
        final byte[] key = writeUtf8("hello");
        final byte[] value = writeUtf8("world");
        backendProxy.bPut(key, value);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        // async get with bytes
        final CompletableFuture<byte[]> f1 = backendProxy.get(key);
        f1.whenComplete((para, param2) -> {
            System.out.println(Arrays.toString(para) + ":" + param2);
            countDownLatch.countDown();
        });
        countDownLatch.await();

    }
}
