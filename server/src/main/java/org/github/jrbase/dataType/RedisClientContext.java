package org.github.jrbase.dataType;

public class RedisClientContext {
    private boolean isLogin = false;
    private RedisClientContext redisClientContext;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public RedisClientContext getRedisClientContext() {
        return redisClientContext;
    }

    public void setRedisClientContext(RedisClientContext redisClientContext) {
        this.redisClientContext = redisClientContext;
    }
}
