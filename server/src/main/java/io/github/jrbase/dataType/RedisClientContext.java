package io.github.jrbase.dataType;

public class RedisClientContext {
    private boolean isLogin = false;
    private RedisClientContext redisClientContext;
    private int dbIndex = 0;

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

    public RedisClientContext getRedisClientContext() {
        return redisClientContext;
    }

    public void setRedisClientContext(RedisClientContext redisClientContext) {
        this.redisClientContext = redisClientContext;
    }
}
