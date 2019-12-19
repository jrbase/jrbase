package org.github.jrbase.dataType;

import io.netty.channel.ChannelHandlerContext;

public class ClientCmd {
    String cmd;
    String[] args;
    ChannelHandlerContext context;

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
