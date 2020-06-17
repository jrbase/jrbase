package io.github.jrbase.server;

import io.github.jrbase.handler.CmdHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;


/**
 *
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private final CmdHandler cmdHandler;

    public ServerHandler(CmdHandler cmdHandler) {
        this.cmdHandler = cmdHandler;
    }

    private static final Logger logger = Logger.getLogger(JRBaseServer.class);


  /*  @Override
    public void channelActive(final ChannelHandlerContext ctx) {

    }*/

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        cmdHandler.removeContext(ctx);
        super.channelInactive(ctx);
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        cmdHandler.handleMsg(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
        logger.error(cause);
    }

}
