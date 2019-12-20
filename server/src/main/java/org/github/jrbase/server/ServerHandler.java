package org.github.jrbase.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.github.jrbase.handler.CmdHandler;

import java.util.logging.Logger;


/**
 *
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = Logger.getLogger(JRBaseServer.class.getName());

    private CmdHandler cmdHandler = new CmdHandler();

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        cmdHandler.handleMsg(ctx, msg);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}