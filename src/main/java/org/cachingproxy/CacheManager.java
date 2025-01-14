package org.cachingproxy;

import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {
    private ConcurrentHashMap<String, CacheObject> caches;

    public CacheManager() {
        this.caches = new ConcurrentHashMap<>();
    }

    public void clearCache() {
        this.caches.clear();
        System.out.println("Cache cleared");
    }

    public CacheObject getCache(String key) {
        return this.caches.get(key);
    }

    public void addCache(String key, CacheObject value) {
        this.caches.put(key, value);
    }

}
