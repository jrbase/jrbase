package io.github.jrbase.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import java.util.List;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private final List<Promise<String>> promiseList;

    int index = 0;

    public ClientHandler(List<Promise<String>> promiseList) {
        this.promiseList = promiseList;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
        if (index < promiseList.size()) {
            promiseList.get(index++).trySuccess(msg);
        } else {
            throw new UnsupportedOperationException("Only support size: " + promiseList.size() + " message, You should add more by io.github.jrbase.client.RedisClient " +
                    "promiseList.add(group.next().newPromise());");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
