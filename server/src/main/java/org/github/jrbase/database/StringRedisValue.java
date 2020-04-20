package org.github.jrbase.database;

import org.github.jrbase.dataType.RedisDataType;

public class StringRedisValue extends RedisValue {
    private String value;

    public StringRedisValue() {
        this.setType(RedisDataType.STRINGS);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
