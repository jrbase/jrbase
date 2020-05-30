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

import io.github.jrbase.common.config.RedisConfigurationOption;
import io.github.jrbase.common.zk.ZKServerRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import static io.github.jrbase.common.config.YamlTool.readConfig;


/**
 * standalone server
 * start the java redis server
 */
public class JRBaseServer extends IServer {

    private ChannelFuture future;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private RedisConfigurationOption config = new RedisConfigurationOption();
    private ZKServerRegister zkServerRegister;

    public JRBaseServer() {

    }

    public JRBaseServer(int port) {
        config.setPort(port);
    }

    public JRBaseServer(String host, int port) {
        config.setBind(host);
        config.setPort(port);
    }

    // if you want to add a config file please add following line to your idea Program arguments
    // server/config/redis_server.yaml
    @Override
    public synchronized void start(String[] args) {

        System.out.println("Start server");
        if (args != null && args.length >= 1) {
            final String confFile = args[0];
            config = readConfig(confFile);
        }

        startCluster();

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer(config));

            future = b.bind(config.getBind(), config.getPort()).sync();

        } catch (Exception e) {
            e.printStackTrace();
            shutdown();
        }

    }

    @Override
    public synchronized void shutdown() {
        System.out.println("Stopping server");
        stopCluster();
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

    @Override
    protected void startCluster() {
        if (config.isCluster()) {
            zkServerRegister = new ZKServerRegister(config);
            zkServerRegister.register();
        }
    }

    @Override
    protected void stopCluster() {
        if (config.isCluster()) {
            zkServerRegister.unRegister();
        }
    }

}
