package org.github.jrbase.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.github.jrbase.config.RedisConfigurationOption;
import org.github.jrbase.handler.CmdHandler;

import java.util.logging.Logger;


/**
 *
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private CmdHandler cmdHandler;

    public ServerHandler(RedisConfigurationOption redisConfigurationOption) {

        this.cmdHandler = new CmdHandler(redisConfigurationOption);
    }

    private static final Logger logger = Logger.getLogger(JRBaseServer.class.getName());


    @Override
    public void channelActive(final ChannelHandlerContext ctx) {

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        cmdHandler.handleMsg(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}