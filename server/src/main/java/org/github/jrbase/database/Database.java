package org.github.jrbase.database;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private final int id;
    private final Map<String, RedisValue> table = new ConcurrentHashMap<>(100);

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
