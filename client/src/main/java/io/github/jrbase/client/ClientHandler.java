package io.github.jrbase.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private BlockingQueue<String> queue;

    private ChannelHandlerContext ctx;

    public ClientHandler(BlockingQueue<String> queue) {
        this.queue = queue;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
        try {
            this.queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(String message) {
        while (ctx == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("等待ChannelHandlerContext实例化过程中出错" + e);
            }
        }
        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
