package org.github.jrbase.dataType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Cmd {
    SET("set"),
    GET("get"),

    OTHER("other");

    private String cmdName;

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

    static {
        for (Cmd c : EnumSet.allOf(Cmd.class)) {
            lookup.put(c.getCmdName(), c);
        }


    }
}
