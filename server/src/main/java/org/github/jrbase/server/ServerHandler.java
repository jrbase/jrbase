package org.github.jrbase.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.manager.CmdManager;

import java.util.logging.Logger;


/**
 *
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = Logger.getLogger(JRBaseServer.class.getName());
    private static final String REPLY_OK = "OK";


    @Override
    public void channelActive(final ChannelHandlerContext ctx) {

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        handleMsg(ctx, msg);
        System.out.println("msg" + msg);

    }


    private void handleMsg(ChannelHandlerContext ctx, String message) {
        final String[] arr = message.split("\r\n");
        if (isHaveCmd(arr.length)) {
            final String cmd = arr[2];
            System.out.println("cmd" + cmd);
            //1. connect
            if ("COMMAND".equals(cmd)) {
                replySimpleStringToClient(ctx, REPLY_OK);
            } else if ("ping".equals(cmd)) {
                replySimpleStringToClient(ctx, "PONG");
            } else {
                ClientCmd clientCmd = translateClientCmd(arr);
                clientCmd.setContext(ctx);
                CmdManager.process(clientCmd);
            }

        } else {
            throw new RuntimeException("string parse error");
        }


    }

    // 0  1  2  3  4  5  6
    //*3 $3 set $1 a $1  b
    private ClientCmd translateClientCmd(String[] arr) {
        ClientCmd clientCmd = new ClientCmd();

        if (isHaveArgs(arr.length)) {
            clientCmd.setCmd(arr[2]);
            final int argsCount = arr.length - 4 / 2;
            String[] args = new String[argsCount];
            int count = 0;
            for (int i = 4; i < arr.length; i = i + 2) {
                args[count++] = arr[i];
            }
            clientCmd.setArgs(args);

        }
        return clientCmd;
    }

    private boolean isHaveCmd(int length) {
        return length >= 3;
    }

    private boolean isHaveArgs(int length) {
        return length > 4;
    }

    private void replySimpleStringToClient(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush("+" + msg + "\r\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}