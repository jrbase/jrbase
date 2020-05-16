package io.github.jrbase.dataType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum RedisDataType {
    /**
     *
     */
    STRINGS("string", "s"),
    HASHES("hash", "h"),
    LISTS("list", "l"),
    SETS("set", "c"),
    SORTED_SETS("zset", "z"),
    NONE("none", "n"),
    KEY("key", "k");

    private final String name;
    private final String abbreviation;

    RedisDataType(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static RedisDataType get(String redisDataTypeName) {
        final RedisDataType redisDataType = LOOK_UP.get(redisDataTypeName);
        return redisDataType == null ? NONE : redisDataType;
    }

    private static final Map<String, RedisDataType> LOOK_UP = new HashMap<>();

    static {
        for (RedisDataType c : EnumSet.allOf(RedisDataType.class)) {
            LOOK_UP.put(c.getName(), c);
        }
    }
}
