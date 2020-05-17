package io.github.jrbase.dataType;

import io.netty.channel.ChannelHandlerContext;

public class RedisClientContext {
    private boolean isLogin = false;
    private int dbIndex = 0;

    private ChannelHandlerContext redisClient;

    public ChannelHandlerContext getRedisClient() {
        return redisClient;
    }

    public void setRedisClient(ChannelHandlerContext redisClient) {
        this.redisClient = redisClient;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

}
