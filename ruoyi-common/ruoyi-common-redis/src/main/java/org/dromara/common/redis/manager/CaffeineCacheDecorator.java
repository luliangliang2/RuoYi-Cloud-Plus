package org.dromara.common.redis.manager;

import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * Cache 装饰器模式(用于扩展 Caffeine 一级缓存)
 *
 * @author LionLi
 */
public class CaffeineCacheDecorator implements Cache {

    private final String name;
    private final Cache cache;
    private final com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeine;

    public CaffeineCacheDecorator(String name, Cache cache, com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeine) {
        this.name = name;
        this.cache = cache;
        this.caffeine = caffeine;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return cache.getNativeCache();
    }

    public String getUniqueKey(Object key) {
        return name + ":" + key;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object o = caffeine.get(getUniqueKey(key), k -> cache.get(key));
        return (ValueWrapper) o;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Class<T> type) {
        Object o = caffeine.get(getUniqueKey(key), k -> cache.get(key, type));
        return (T) o;
    }

    @Override
    public void put(Object key, Object value) {
        caffeine.invalidate(getUniqueKey(key));
        cache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        caffeine.invalidate(getUniqueKey(key));
        return cache.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        evictIfPresent(key);
    }

    @Override
    public boolean evictIfPresent(Object key) {
        boolean b = cache.evictIfPresent(key);
        if (b) {
            caffeine.invalidate(getUniqueKey(key));
        }
        return b;
    }

    @Override
    public void clear() {
        clearLocalCache();
        cache.clear();
    }

    @Override
    public boolean invalidate() {
        boolean invalidated = cache.invalidate();
        if (invalidated) {
            clearLocalCache();
        }
        return invalidated;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object o = caffeine.get(getUniqueKey(key), k -> cache.get(key, valueLoader));
        return (T) o;
    }

    private void clearLocalCache() {
        String prefix = name + ":";
        caffeine.asMap().keySet().removeIf(key -> key instanceof String cacheKey && cacheKey.startsWith(prefix));
    }

}
