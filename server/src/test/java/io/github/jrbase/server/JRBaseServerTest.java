package io.github.jrbase.server;

import io.github.jrbase.JRServerEmbedded;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class JRBaseServerTest {
    private static final JRServerEmbedded jrServerEmbedded = new JRServerEmbedded("6379");

    @BeforeClass
    public static void before() throws IOException {
        jrServerEmbedded.start();
    }

    @AfterClass
    public static void end() {
        jrServerEmbedded.stop();
    }

    @Test
    public void testAccessRedis() throws InterruptedException {
        Thread.sleep(1000);
    }
}
