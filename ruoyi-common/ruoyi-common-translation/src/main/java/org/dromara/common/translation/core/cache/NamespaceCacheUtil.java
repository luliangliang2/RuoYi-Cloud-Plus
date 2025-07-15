package org.dromara.common.translation.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author zc
 * 命名空间缓存工具类 支持多级缓存结构，按命名空间组织缓存数据
 *
 */
public class NamespaceCacheUtil {

    // 私有构造器防止实例化
    private NamespaceCacheUtil() {
    }
    // 默认缓存配置
    private static final CacheConfig DEFAULT_CONFIG = new CacheConfig(1000, 10, TimeUnit.MINUTES);

    // 外层缓存：命名空间 -> 该命名空间下的缓存
    private static final Cache<String, Cache<String, Object>> NAMESPACE_CACHE = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.HOURS)
        .maximumSize(100)
        .build();

    // 各命名空间的独立配置
    private static final Map<String, CacheConfig> NAMESPACE_CONFIGS = new ConcurrentHashMap<>();

    /**
     * 配置命名空间缓存参数
     *
     * @param namespace 命名空间名称
     * @param maxSize   最大缓存条目数
     * @param duration  持续时间
     * @param unit      时间单位
     */
    public static void configNamespace(String namespace, int maxSize, long duration,
        TimeUnit unit) {
        NAMESPACE_CONFIGS.put(namespace, new CacheConfig(maxSize, duration, unit));
    }

    /**
     * 存入缓存
     *
     * @param namespace 命名空间
     * @param key       缓存键
     * @param value     缓存值
     */
    public static void put(String namespace, String key, Object value) {
        getNamespaceCache(namespace).put(key, value);
    }

    /**
     * 获取缓存值
     *
     * @param namespace 命名空间
     * @param key       缓存键
     * @return 缓存值，不存在返回null
     */
    public static Object get(String namespace, String key) {
        Cache<String, Object> cache = NAMESPACE_CACHE.getIfPresent(namespace);
        return cache == null ? null : cache.getIfPresent(key);
    }

    /**
     * 获取缓存值，不存在时通过loader加载
     *
     * @param namespace 命名空间
     * @param key       缓存键
     * @param loader    加载函数
     * @return 缓存值
     */
    public static Object getOrLoad(String namespace, String key, Function<String, Object> loader) {
        return getNamespaceCache(namespace).get(key, loader);
    }

    /**
     * 获取命名空间下的所有缓存项
     *
     * @param namespace 命名空间
     * @return 不可修改的Map视图
     */
    public static Map<String, Object> getAll(String namespace) {
        Cache<String, Object> cache = NAMESPACE_CACHE.getIfPresent(namespace);
        return cache == null ? Collections.emptyMap() : Collections.unmodifiableMap(cache.asMap());
    }

    /**
     * 移除指定缓存项
     *
     * @param namespace 命名空间
     * @param key       缓存键
     */
    public static void remove(String namespace, String key) {
        Cache<String, Object> cache = NAMESPACE_CACHE.getIfPresent(namespace);
        if (cache != null) {
            cache.invalidate(key);
        }
    }

    /**
     * 清空指定命名空间
     *
     * @param namespace 命名空间
     */
    public static void clear(String namespace) {
        NAMESPACE_CACHE.invalidate(namespace);
    }

    /**
     * 清空所有缓存
     */
    public static void clearAll() {
        NAMESPACE_CACHE.invalidateAll();
    }

    /**
     * 获取所有命名空间名称
     *
     * @return 不可修改的Set视图
     */
    public static Set<String> namespaces() {
        return Collections.unmodifiableSet(NAMESPACE_CACHE.asMap().keySet());
    }

    /**
     * 获取命名空间缓存统计信息
     *
     * @param namespace 命名空间
     * @return 统计信息，命名空间不存在返回null
     */
    public static CacheStats stats(String namespace) {
        Cache<String, Object> cache = NAMESPACE_CACHE.getIfPresent(namespace);
        return cache == null ? null : cache.stats();
    }

    // 获取或创建命名空间缓存
    private static Cache<String, Object> getNamespaceCache(String namespace) {
        return NAMESPACE_CACHE.get(namespace, ns -> {
            CacheConfig config = NAMESPACE_CONFIGS.getOrDefault(namespace, DEFAULT_CONFIG);
            return Caffeine.newBuilder()
                .maximumSize(config.maxSize)
                .expireAfterWrite(config.duration, config.unit)
                .recordStats()
                .build();
        });
    }

    // 缓存配置类
    private static class CacheConfig {

        final int maxSize;
        final long duration;
        final TimeUnit unit;

        CacheConfig(int maxSize, long duration, TimeUnit unit) {
            this.maxSize = maxSize;
            this.duration = duration;
            this.unit = unit;
        }
    }
}
