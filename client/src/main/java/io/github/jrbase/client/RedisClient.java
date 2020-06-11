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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

public class RedisClient {
    private final EventLoopGroup group;
    private ChannelFuture channelFuture;
    private BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);


    private final ClientHandler clientHandler;

    public RedisClient() {
        this("localhost", 6379);
//        this("192.168.100.1", 6379);
    }

    public RedisClient(String host, int port) {
        group = new NioEventLoopGroup();

        clientHandler = new ClientHandler(queue);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
            stop();
        } finally {
            System.out.println("connect");
        }

    }

    public String sendMessage(String message) {
        String redisCommand = RedisRequestCommand.toRedisCommand(message);
        clientHandler.sendMessage(redisCommand);
        return receiveMsg();
    }

    public String receiveMsg() {
        String take = "";
        try {
            take = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return take;
    }

    public void stop() {
        try {
            group.shutdownGracefully().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final RedisClient redisClient = new RedisClient("192.168.100.1", 6379);
        String message = redisClient.sendMessage("*2\r\n$4\r\nping\r\n$3\r\n123\r\n");
        System.out.println(message);

        message = redisClient.sendMessage("*2\r\n$4\r\nping\r\n$3\r\n456\r\n");
        System.out.println(message);

        redisClient.stop();
    }

}
