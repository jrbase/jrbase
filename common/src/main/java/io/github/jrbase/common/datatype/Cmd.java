package io.github.jrbase.common.datatype;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Cmd {
    // Keys
    KEYS("keys", RedisDataType.KEY),
    TYPE("type", RedisDataType.KEY),
    EXPIRE("expire", RedisDataType.KEY),
    // Strings
    SET("set", RedisDataType.STRINGS),
    GET("get", RedisDataType.STRINGS),
    MSET("mset", RedisDataType.STRINGS),
    MGET("mget", RedisDataType.STRINGS),
    GETBIT("getbit", RedisDataType.STRINGS),
    SETBIT("setbit", RedisDataType.STRINGS),
    // Hashes
    HSET("hset", RedisDataType.HASHES),
    HGET("hget", RedisDataType.HASHES),
    HLEN("hlen", RedisDataType.HASHES),
    // Lists
    LPUSH("lpush", RedisDataType.LISTS),
    LPOP("lpop", RedisDataType.LISTS),
    LRANGE("lrange", RedisDataType.LISTS),
    RPUSH("rpush", RedisDataType.LISTS),
    RPOP("rpop", RedisDataType.LISTS),
    //Sets
    SADD("sadd", RedisDataType.SETS),
    SPOP("spop", RedisDataType.SETS),
    SCARD("scard", RedisDataType.SETS),

    //Sets
    ZADD("zadd", RedisDataType.SORTED_SETS),
    ZRANGE("zrange", RedisDataType.SORTED_SETS),

    // others
    OTHER("other", RedisDataType.NONE);

    private final String cmdName;

    public RedisDataType getType() {
        return type;
    }

    private final RedisDataType type;

    Cmd(String cmdName, RedisDataType type) {
        this.cmdName = cmdName;
        this.type = type;
    }

    public String getCmdName() {
        return cmdName;
    }

    public static Cmd get(String cmdName) {
        final Cmd cmd = LOOKUP.get(cmdName);
        return cmd == null ? OTHER : cmd;
    }

    private static final Map<String, Cmd> LOOKUP = new HashMap<>();

    public static Map<String, Cmd> getLookup() {
        return LOOKUP;
    }

    static {
        for (Cmd c : EnumSet.allOf(Cmd.class)) {
            LOOKUP.put(c.getCmdName(), c);
        }
    }
}
