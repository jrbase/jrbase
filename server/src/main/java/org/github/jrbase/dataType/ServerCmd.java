package org.github.jrbase.dataType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ServerCmd {
    //Connection
    AUTH("auth"),
    ECHO("echo"),
    PING("ping"),
    SELECT("select"),
    DBSIZE("dbsize"),
    //Server
    COMMAND("command"),
    //Other
    CONFIG("config"),
    UNKNOWN("other");

    private final String cmdName;

    ServerCmd(String cmdName) {
        this.cmdName = cmdName;
    }

    public String getCmdName() {
        return cmdName;
    }

    public static ServerCmd get(String cmdName) {
        final ServerCmd cmd = LOOKUP.get(cmdName);
        return cmd == null ? UNKNOWN : cmd;
    }

    private static final Map<String, ServerCmd> LOOKUP = new HashMap<>();

    public static Map<String, ServerCmd> getLookup() {
        return LOOKUP;
    }

    static {
        for (ServerCmd c : EnumSet.allOf(ServerCmd.class)) {
            LOOKUP.put(c.getCmdName(), c);
        }
    }
}
