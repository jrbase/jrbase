package org.github.jrbase.dataType;

import io.netty.channel.Channel;
import org.github.jrbase.backend.BackendProxy;

import java.util.Arrays;

public class ClientCmd {
    private String cmd;
    private String key;
    private String[] args;
    private Channel channel;
    private BackendProxy backendProxy;

    public ClientCmd() {

    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public BackendProxy getBackendProxy() {
        return backendProxy;
    }

    public void setBackendProxy(BackendProxy backendProxy) {
        this.backendProxy = backendProxy;
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
