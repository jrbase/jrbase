package io.github.jrbase.database;

import io.github.jrbase.common.datatype.RedisDataType;

import java.util.HashMap;
import java.util.Map;

public class HashRedisValue extends RedisValue {
    public HashRedisValue() {
        this.setType(RedisDataType.HASHES);
    }

    private final Map<String, String> hash = new HashMap<>();

    public Map<String, String> getHash() {
        return hash;
    }
}
