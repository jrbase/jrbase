package org.github.jrbase.database;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final int id;
    private final Map<String, RedisValue> table = new HashMap<>();

    public Database(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Map<String, RedisValue> getTable() {
        return table;
    }
}
