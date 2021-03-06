package io.github.jrbase.database;

import io.github.jrbase.common.datatype.RedisDataType;

public class RedisValue {
    private RedisDataType type;
    private long expire;

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public RedisDataType getType() {
        return type;
    }

    public void setType(RedisDataType type) {
        this.type = type;
    }
}
