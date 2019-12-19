package org.github.jrbase.process;

import io.netty.channel.ChannelHandlerContext;
import org.github.jrbase.dataType.ClientCmd;

public class GetProcess implements CmdProcess {
    private ChannelHandlerContext context;

    @Override
    public void process(ClientCmd clientCmd) {
        context = clientCmd.getContext();

        CmdProcess.super.process(clientCmd);
    }

    @Override
    public void requestKV() {

    }

    @Override
    public void replyClient() {
        context.channel().writeAndFlush(":1\r\n");

    }
}
