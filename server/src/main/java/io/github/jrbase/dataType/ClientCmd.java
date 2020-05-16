package io.github.jrbase.dataType;

import io.github.jrbase.database.Database;
import io.netty.channel.Channel;

import java.util.Arrays;

public class ClientCmd {
    private String cmd;
    private String key;
    private String[] args;
    private Channel channel;
    private RedisClientContext redisClientContext;

    private Database db;
    private String error;

    public Database getDb() {
        return db;
    }

    public void setDb(Database db) {
        this.db = db;
    }

    public ClientCmd() {

    }

    public RedisClientContext getRedisClientContext() {
        return redisClientContext;
    }

    public void setRedisClientContext(RedisClientContext redisClientContext) {
        this.redisClientContext = redisClientContext;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
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

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
