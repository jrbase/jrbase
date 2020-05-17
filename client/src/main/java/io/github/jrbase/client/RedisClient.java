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
import io.netty.util.concurrent.Promise;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RedisClient {
    private final EventLoopGroup group;
    private ChannelFuture channelFuture;


    public List<Promise<String>> getPromiseList() {
        return promiseList;
    }

    private final List<Promise<String>> promiseList = new ArrayList<>();

    public RedisClient() {
        this("localhost", 6379);
    }

    public RedisClient(String host, int port) {
        group = new NioEventLoopGroup();
        promiseList.add(group.next().newPromise());
        promiseList.add(group.next().newPromise());
        promiseList.add(group.next().newPromise());
        promiseList.add(group.next().newPromise());
        promiseList.add(group.next().newPromise());

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
                    pipeline.addLast(new ClientHandler(promiseList));
                }
            });
            channelFuture = clientBootstrap.connect().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            stop();
        } finally {
            System.out.println("connect");
        }

    }

    public void sendMessage(String message) {
        channelFuture.channel().writeAndFlush(message);
    }

    public void sendMessage(RedisCommand redisCommand) {
        channelFuture.channel().writeAndFlush(redisCommand.toString());
    }

    public void stop() {
        try {
            group.shutdownGracefully().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final RedisClient redisClient = new RedisClient();
        redisClient.sendMessage("*2\r\n$4\r\nping\r\n");

        final String message = redisClient.getPromise().get();
        System.out.println(message);
        redisClient.stop();
    }

    public Promise<String> getPromise() {
        return promiseList.get(0);
    }

}
