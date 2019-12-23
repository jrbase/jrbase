package org.github.jrbase.handler;

import io.netty.channel.ChannelHandlerContext;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.manager.CmdManager;

public class CmdHandler {

    private static final String REPLY_OK = "OK";


    public void handleMsg(ChannelHandlerContext ctx, String message) {
        final ClientCmd clientCmd = parseMessage(message);
        //1. connect
        if (clientCmd.getCmd().isEmpty()) {
            replyErrorToClient(ctx, "empty command");
        } else if ("COMMAND".equals(clientCmd.getCmd())) {
            replyInfoToClient(ctx, REPLY_OK);
        } else if ("ping".equals(clientCmd.getCmd())) {
            replyInfoToClient(ctx, "PONG");
        } else {
            clientCmd.setContext(ctx);
            CmdManager.process(clientCmd);
        }

    }

    public ClientCmd parseMessage(String message) {
        final String[] arr = message.split("\r\n");

        return translateClientCmd(arr);

    }

    //             key   value
    // 0  1  2  3  4  5  6
    //*3 $3 set $1 a $1  b
    public ClientCmd translateClientCmd(String[] arr) {
        ClientCmd clientCmd = new ClientCmd();
        clientCmd.setCmd("");
        clientCmd.setKey("");
        clientCmd.setArgs(new String[0]);

        if (isHaveCmd(arr.length)) {
            final String cmd = arr[2];
            clientCmd.setCmd(cmd);
            if (isHaveKey(arr.length)) {
                clientCmd.setKey(arr[4]);
                if (isHaveArgs(arr.length)) {
                    final int argsCount = (arr.length + 1 - 6) / 2;
                    String[] args = new String[argsCount];
                    int count = 0;
                    for (int i = 6; i < arr.length; i = i + 2) {
                        args[count++] = arr[i];
                    }
                    clientCmd.setArgs(args);
                }
            }
        }
        return clientCmd;
    }

    private boolean isHaveCmd(int length) {
        return length >= 3;
    }

    private boolean isHaveKey(int length) {
        return length > 4;
    }

    private boolean isHaveArgs(int length) {
        return length > 6;
    }

    private void replyInfoToClient(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush("+" + msg + "\r\n");
    }

    private void replyErrorToClient(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush("-" + msg + "\r\n");
    }

}
