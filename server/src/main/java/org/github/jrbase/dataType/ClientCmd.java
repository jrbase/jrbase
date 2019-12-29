package org.github.jrbase.dataType;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.ChannelHandlerContext;

public class ClientCmd {
    private String cmd;
    private String key;
    private String[] args;
    private ChannelHandlerContext context;
    private RheaKVStore rheaKVStore;

    public ClientCmd() {

    }

    public String getKeyAddAbbreviation(String abbreviation) {
        return this.key + abbreviation;
    }

    public RheaKVStore getRheaKVStore() {
        return rheaKVStore;
    }

    public void setRheaKVStore(RheaKVStore rheaKVStore) {
        this.rheaKVStore = rheaKVStore;
    }

    public int getArgLength() {
        return args != null ? args.length : 0;
    }

    public ClientCmd(String cmd) {
        this.cmd = cmd;
    }

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
