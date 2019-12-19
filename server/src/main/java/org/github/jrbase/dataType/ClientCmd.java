package org.github.jrbase.dataType;

import io.netty.channel.ChannelHandlerContext;
import org.github.jrbase.proxyRheakv.rheakv.Client;

public class ClientCmd {
    private String cmd;
    private String key;
    private String[] args;
    private ChannelHandlerContext context;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
