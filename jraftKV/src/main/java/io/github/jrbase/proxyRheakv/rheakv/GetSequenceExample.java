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
import com.alipay.sofa.jraft.rhea.storage.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;

/**
 *
 * @author jiachun.fjc
 */
public class GetSequenceExample {

    private static final Logger LOG = LoggerFactory.getLogger(GetSequenceExample.class);

    public static void main(final String[] args) {
        final Client client = new Client();
        client.init();
        getSequence(client.getRheaKVStore());
        client.shutdown();
    }

    public static void getSequence(final RheaKVStore backendProxy) {
        final byte[] key = writeUtf8("sequence");
        final CompletableFuture<Sequence> sequence = backendProxy.getSequence(key, 10);

        // async
        final CompletableFuture<Sequence> f1 = backendProxy.getSequence(key, 20);
        final CompletableFuture<Sequence> f2 = backendProxy.getSequence("sequence", 30);
        CompletableFuture.allOf(f1, f2).join();
        LOG.info("Async getSequence result={}", f1.join());
        LOG.info("Async getSequence result={}", f2.join());

        final CompletableFuture<Boolean> f3 = backendProxy.resetSequence(key);
        f3.join();

        // sync
        final Sequence b1 = backendProxy.bGetSequence(key, 40);
        final Sequence b2 = backendProxy.bGetSequence("sequence", 50);
        LOG.info("Sync getSequence result={}", b1);
        LOG.info("Sync getSequence result={}", b2);

        backendProxy.bResetSequence(key);
    }
}
