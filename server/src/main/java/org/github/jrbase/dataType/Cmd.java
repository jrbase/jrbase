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
        final Cmd cmd = lookup.get(cmdName);
        return cmd == null ? OTHER : cmd;
    }

    private static final Map<String, Cmd> lookup = new HashMap<>();

    public static Map<String, Cmd> getLookup() {
        return lookup;
    }

    static {
        for (Cmd c : EnumSet.allOf(Cmd.class)) {
            lookup.put(c.getCmdName(), c);
        }
    }
}
