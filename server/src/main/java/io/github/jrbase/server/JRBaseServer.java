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

package io.github.jrbase.server;

import io.github.jrbase.config.RedisConfigurationOption;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import static io.github.jrbase.config.YamlTool.readConfig;

/**
 * standalone server
 * second: start the java redis server
 */
public class JRBaseServer {

    private ChannelFuture future;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private RedisConfigurationOption redisConfigurationOption = new RedisConfigurationOption();

    public JRBaseServer() {

    }

    public JRBaseServer(int port) {
        redisConfigurationOption.setPort(port);
    }

    public JRBaseServer(String host, int port) {
        redisConfigurationOption.setBind(host);
        redisConfigurationOption.setPort(port);
    }

    // if you want to add a config file please add following line to your idea Program arguments
    // server/config/redis_server.yaml
    public synchronized void start(String[] args) {

        System.out.println("Start server");
        if (args != null && args.length >= 1) {
            final String confFile = args[0];
            redisConfigurationOption = readConfig(confFile);
        }

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer(redisConfigurationOption));

            future = b.bind(redisConfigurationOption.getBind(), redisConfigurationOption.getPort()).sync();

        } catch (Exception e) {
            e.printStackTrace();
            shutdown();
        }

    }

    public synchronized void shutdown() {
        System.out.println("Stopping server");
        try {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Server stopped");
        }
    }

}
