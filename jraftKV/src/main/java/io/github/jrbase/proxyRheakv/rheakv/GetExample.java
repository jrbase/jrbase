/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.jrbase.proxyRheakv.rheakv;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;

/**
 *
 * @author jiachun.fjc
 */
public class GetExample {

    private static final Logger LOG = LoggerFactory.getLogger(GetExample.class);

    public static void main(final String[] args) {
        final Client client = new Client();
        client.init();
        get(client.getRheaKVStore());
        client.shutdown();
    }

    public static void get(final RheaKVStore backendProxy) {
        final byte[] key = writeUtf8("hello");
        final byte[] value = writeUtf8("world");
        backendProxy.bPut(key, value);

        // async get with bytes
        final CompletableFuture<byte[]> f1 = backendProxy.get(key);
        final CompletableFuture<byte[]> f2 = backendProxy.get(key, false);
        // async get with string
        final CompletableFuture<byte[]> f3 = backendProxy.get("hello");
        final CompletableFuture<byte[]> f4 = backendProxy.get("hello", false);
        CompletableFuture.allOf(f1, f2, f3, f4).join();
        LOG.info("Async get result={}", readUtf8(f1.join()));
        LOG.info("Async get result={}", readUtf8(f2.join()));
        LOG.info("Async get result={}", readUtf8(f3.join()));
        LOG.info("Async get result={}", readUtf8(f4.join()));

        // sync get with bytes
        final byte[] b1 = backendProxy.bGet(key);
        final byte[] b2 = backendProxy.bGet(key, false);
        // sync get with string
        final byte[] b3 = backendProxy.bGet("hello");
        final byte[] b4 = backendProxy.bGet("hello", false);
        LOG.info("Sync get result={}", readUtf8(b1));
        LOG.info("Sync get result={}", readUtf8(b2));
        LOG.info("Sync get result={}", readUtf8(b3));
        LOG.info("Sync get result={}", readUtf8(b4));
    }
}
