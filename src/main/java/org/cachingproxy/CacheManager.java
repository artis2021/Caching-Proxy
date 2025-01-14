package org.cachingproxy;

import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {
    private final ConcurrentHashMap<String, CacheObject> cache;

    public CacheManager() {
        this.cache = new ConcurrentHashMap<>();
    }

    public void clearCache() {
        cache.clear();
        System.out.println("Cache cleared successfully.");
    }

    public CacheObject getCache(String key) {
        return cache.get(key);
    }

    public void addCache(String key, CacheObject cacheObject) {
        cache.put(key, cacheObject);
    }
}