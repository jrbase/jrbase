package io.github.jrbase.database;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private final int id;
    private final Map<String, RedisValue> table;

    public Database(int id) {
        this.id = id;
        this.table = new ConcurrentHashMap<>(1024);
    }

    public Database(int id, int size) {
        this.id = id;
        this.table = new ConcurrentHashMap<>(size);
    }

    public int getId() {
        return id;
    }

    public void clear() {
        table.clear();
    }

    public RedisValue get(String key) {
        return table.get(key);
    }

    public int size() {
        return table.size();
    }

    public void put(String key, RedisValue redisValue) {
        table.put(key, redisValue);
    }

    public Set<String> keySet() {
        return table.keySet();
    }

    public RedisValue getOrDefault(String key, RedisValue redisValue) {
        return table.getOrDefault(key, redisValue);
    }
}
