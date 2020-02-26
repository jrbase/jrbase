package org.github.jrbase.tikv;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.tikv.common.TiConfiguration;
import org.tikv.common.TiSession;
import org.tikv.raw.RawKVClient;
import shade.com.google.protobuf.ByteString;

public class ClientTest {

    @Test
    @Ignore
    public void testClient() {

        TiConfiguration conf = TiConfiguration.createRawDefault("127.0.0.1:2379");
        TiSession session = TiSession.create(conf);
        RawKVClient rawClient = session.createRawClient();
        // put
        rawClient.put(ByteString.copyFromUtf8("123"), ByteString.copyFromUtf8("456"));
        // get
        ByteString bytes = rawClient.get(ByteString.copyFromUtf8("123"));
        System.out.println("end");
        Assert.assertEquals("456", bytes.toStringUtf8());

    }

}
