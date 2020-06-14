package io.github.jrbase.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private final BlockingQueue<String> queue;

    private ChannelHandlerContext ctx;

    private final RedisClient redisClient;

    public ClientHandler(BlockingQueue<String> queue, RedisClient redisClient) {
        this.queue = queue;
        this.redisClient = redisClient;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
        redisClient.setActive(true);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        redisClient.setActive(false);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
        try {
            this.queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        while (ctx == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("ClientHandler#sendMessage " + e);
            }
        }
        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
