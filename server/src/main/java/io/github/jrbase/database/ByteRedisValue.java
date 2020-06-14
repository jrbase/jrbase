package io.github.jrbase.database;

import io.github.jrbase.common.datatype.RedisDataType;

public class ByteRedisValue extends RedisValue {
    private byte[] value;

    public ByteRedisValue() {
        this.setType(RedisDataType.STRINGS);
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
