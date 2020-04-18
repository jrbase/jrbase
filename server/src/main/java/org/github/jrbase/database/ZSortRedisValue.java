package org.github.jrbase.database;

import org.github.jrbase.dataType.RedisDataType;

import java.util.TreeMap;

public class ZSortRedisValue extends RedisValue {

    private final TreeMap<String, Integer> value = new TreeMap<>();

    public ZSortRedisValue() {
        this.setType(RedisDataType.SORTED_SETS);
    }

    public TreeMap<String, Integer> getValue() {
        return value;
    }
}
