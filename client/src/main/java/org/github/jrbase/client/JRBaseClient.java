/*
 * Copyright 2015 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.github.jrbase.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.github.jrbase.proto.CmdServiceGrpc;
import org.github.jrbase.proto.Request;
import org.github.jrbase.proto.Response;
import org.github.jrbase.terminal.JRHistory;
import org.github.jrbase.utils.MyStringUtils;
import org.jline.reader.*;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class JRBaseClient {
    private static final Logger logger = Logger.getLogger(JRBaseClient.class.getName());

    private final ManagedChannel channel;
    private final CmdServiceGrpc.CmdServiceBlockingStub blockingStub;

    /**
     * Construct client connecting to HelloWorld server at {@code host:port}.
     */
    public JRBaseClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build());
    }

    /**
     * Construct client for accessing HelloWorld server using the existing channel.
     */
    JRBaseClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = CmdServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     *
     */
    public void sendCmd(int cmd, String key, String value) {
        logger.info("key " + key + " ...");
        Request request = Request.newBuilder()
                .setCmd(cmd)
                .setKey(key)
                .setValueStr(value)
                .build();
        Response response;
        try {
            response = blockingStub.requestCmd(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("result: " + response.getValue());
    }

    /**
     *
     */
    public static void main(String[] args) throws Exception {
        JRBaseClient client = new JRBaseClient("localhost", 20040);
        try {
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            Completer strSet = new ArgumentCompleter(
                    new StringsCompleter("set")
                    , NullCompleter.INSTANCE
            );
            Completer strGet = new ArgumentCompleter(
                    new StringsCompleter("get")
                    , NullCompleter.INSTANCE
            );

            Completer lpush = new ArgumentCompleter(
                    new StringsCompleter("lpush")
                    , NullCompleter.INSTANCE
            );
            Completer lpop = new ArgumentCompleter(
                    new StringsCompleter("lpop")
                    , NullCompleter.INSTANCE
            );

            Completer hset = new ArgumentCompleter(
                    new StringsCompleter("hset")
                    , NullCompleter.INSTANCE
            );
            Completer hget = new ArgumentCompleter(
                    new StringsCompleter("hget")
                    , NullCompleter.INSTANCE
            );

            Completer sadd = new ArgumentCompleter(
                    new StringsCompleter("sadd")
                    , NullCompleter.INSTANCE
            );
            Completer spop = new ArgumentCompleter(
                    new StringsCompleter("spop")
                    , NullCompleter.INSTANCE

            );

            Completer fogCompleter = new AggregateCompleter(
                    strSet, strGet
                    , lpush, lpop
                    , hget, hset
                    , sadd, spop
            );

            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(fogCompleter)
                    .history(new JRHistory())
                    .build();

            String prompt = "JRBase> ";
            while (true) {
                Thread.sleep(1000);
                String line;
                try {
                    line = lineReader.readLine(prompt);
                    if ("bye".equals(line.toLowerCase())) {
                        break;
                    }
                    final String[] cmdAndArgs = MyStringUtils.getCmdAndArgs(line);
                    if (cmdAndArgs.length >= 2) {
                        int cmd = translateCmd(cmdAndArgs[0]);
                        String key = cmdAndArgs[1];
                        if (cmdAndArgs.length >= 3) {
                            client.sendCmd(cmd, key, cmdAndArgs[2]);
                        }else {
                            client.sendCmd(cmd, key, "");
                        }

                    } else {
                        logger.info("error");
                    }


                } catch (UserInterruptException e) {
                    // Do nothing
                } catch (EndOfFileException e) {
                    return;
                }
            }

        } finally {
            client.shutdown();
        }
    }

    private static int translateCmd(String cmdStr) {
        int result = 0;
        switch (cmdStr) {
            case "set": {
                result = 1;
                break;
            }
            case "get": {
                result = 2;
                break;
            }
        }
        return result;
    }
}
