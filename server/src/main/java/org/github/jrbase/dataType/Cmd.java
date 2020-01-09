package org.github.jrbase.dataType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Cmd {
    // Keys
    TYPE("type"),
    // Strings
    SET("set"),
    GET("get"),
    MSET("mset"),
    MGET("mget"),
    GETBIT("getbit"),
    SETBIT("setbit"),
    // Hashes
    HSET("hset"),
    HGET("hget"),
    HLEN("hlen"),
    // Lists
    LPUSH("lpush"),
    LPOP("lpop"),
    LRANGE("lrange"),
    RPUSH("rpush"),
    RPOP("rpop"),
    //Sets
    SADD("sadd"),
    SPOP("spop"),
    SCARD("scard"),

    //Sets
    ZADD("zadd"),

    // others
    OTHER("other");

    final private String cmdName;

    Cmd(String cmdName) {
        this.cmdName = cmdName;
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
