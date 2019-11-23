package org.github.jrbase.db;

import org.github.jrbase.dataType.ScoreSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CacheData {
    private static CacheData cacheData;

    public static CacheData getInstance() {
        if (null == cacheData) {
            cacheData = new CacheData();
        }
        return cacheData;
    }

    private Map<String, String> mapCache = new HashMap<>();
    private Map<String, List<String>> listStrCache = new HashMap<>();
    private Map<String, List<Integer>> listIntCache = new HashMap<>();
    private Map<String, Set<String>> setCache = new HashMap<>();
    private Map<String, List<ScoreSet>> sortCache = new HashMap<>();
    private Map<String, Map<String, String>> mapMapCache = new HashMap<>();

    public Map<String, String> getMapCache() {
        return mapCache;
    }

    public void setMapCache(Map<String, String> mapCache) {
        this.mapCache = mapCache;
    }

    public Map<String, List<String>> getListStrCache() {
        return listStrCache;
    }

    public void setListStrCache(Map<String, List<String>> listStrCache) {
        this.listStrCache = listStrCache;
    }

    public Map<String, List<Integer>> getListIntCache() {
        return listIntCache;
    }

    public void setListIntCache(Map<String, List<Integer>> listIntCache) {
        this.listIntCache = listIntCache;
    }

    public Map<String, Set<String>> getSetCache() {
        return setCache;
    }

    public void setSetCache(Map<String, Set<String>> setCache) {
        this.setCache = setCache;
    }

    public Map<String, List<ScoreSet>> getSortCache() {
        return sortCache;
    }

    public void setSortCache(Map<String, List<ScoreSet>> sortCache) {
        this.sortCache = sortCache;
    }

    public Map<String, Map<String, String>> getMapMapCache() {
        return mapMapCache;
    }

    public void setMapMapCache(Map<String, Map<String, String>> mapMapCache) {
        this.mapMapCache = mapMapCache;
    }
}
