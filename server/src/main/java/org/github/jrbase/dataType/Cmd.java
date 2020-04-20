package org.github.jrbase.dataType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static org.github.jrbase.dataType.RedisDataType.*;

public enum Cmd {
    // Keys
    KEYS("keys", KEY),
    TYPE("type", KEY),
    EXPIRE("expire", KEY),
    // Strings
    SET("set", STRINGS),
    GET("get", STRINGS),
    MSET("mset", STRINGS),
    MGET("mget", STRINGS),
    GETBIT("getbit", STRINGS),
    SETBIT("setbit", STRINGS),
    // Hashes
    HSET("hset", HASHES),
    HGET("hget", HASHES),
    HLEN("hlen", HASHES),
    // Lists
    LPUSH("lpush", LISTS),
    LPOP("lpop", LISTS),
    LRANGE("lrange", LISTS),
    RPUSH("rpush", LISTS),
    RPOP("rpop", LISTS),
    //Sets
    SADD("sadd", SETS),
    SPOP("spop", SETS),
    SCARD("scard", SETS),

    //Sets
    ZADD("zadd", SORTED_SETS),

    // others
    OTHER("other", NONE);

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
