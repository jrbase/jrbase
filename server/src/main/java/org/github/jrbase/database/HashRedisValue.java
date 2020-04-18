package org.github.jrbase.database;

import org.github.jrbase.dataType.RedisDataType;

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
