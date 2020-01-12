package org.github.jrbase.dataType;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;

import java.util.Arrays;

public class ClientCmd {
    private String cmd;
    private String key;
    private String[] args;
    private Channel channel;
    private RheaKVStore rheaKVStore;

    public ClientCmd() {

    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
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

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        if (cmd != null) {
            this.cmd = cmd.toLowerCase();
        } else {
            this.cmd = null;
        }
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

    @Override
    public String toString() {
        return "ClientCmd{" +
                "cmd='" + cmd + '\'' +
                ", key='" + key + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
