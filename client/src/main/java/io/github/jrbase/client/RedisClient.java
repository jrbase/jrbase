package io.github.jrbase.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RedisClient {

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
    private boolean active = false;
    private ChannelFuture channelFuture;
    private EventLoopGroup group;
    private ClientHandler clientHandler;

    private String host;
    private int port;
    private long timeout = 2000;

    public RedisClient() {
        this("localhost", 6379);
    }

    public RedisClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static RedisClient builder() {
        return new RedisClient();
    }

    public RedisClient host(String host) {
        this.host = host;
        return this;
    }

    public RedisClient port(int port) {
        this.port = port;
        return this;
    }

    public RedisClient timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void connect() {
        group = new NioEventLoopGroup();

        clientHandler = new ClientHandler(queue, this);
        try {
            Bootstrap clientBootstrap = new Bootstrap();

            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.remoteAddress(new InetSocketAddress(host, port));
            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) {
                    final ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new StringDecoder());
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(clientHandler);
                }
            });
            channelFuture = clientBootstrap.connect().sync();
        } catch (Exception e) {
//            e.printStackTrace();
            stop();
            active = false;
//            System.out.println("cant connect");
        }
    }

    public void sendMessage(String message) {
        checkConnect();
        String redisCommand = RedisRequestCommand.toRedisCommand(message);
        clientHandler.sendMessage(redisCommand);
    }

    public String sendMessageAndReceive(String message) {
        checkConnect();
        String redisCommand = RedisRequestCommand.toRedisCommand(message);
        clientHandler.sendMessage(redisCommand);
        return receiveMsg();
    }

    private void checkConnect() {
        if (!active) {
            connect();
        }
    }

    public String receiveMsg() {
        String message = "";
        try {
            if (timeout > 0) {
                message = queue.poll(2000, TimeUnit.MILLISECONDS);
            } else {
                message = queue.take();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return message;
    }

    public String receiveMsg(long timeout, TimeUnit timeUnit) {
        String message = "";
        try {
            message = queue.poll(timeout, timeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void stop() {
        try {
            if (group != null) {
                group.shutdownGracefully().sync();
            }
            if (channelFuture != null) {
                channelFuture.channel().closeFuture().sync();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
