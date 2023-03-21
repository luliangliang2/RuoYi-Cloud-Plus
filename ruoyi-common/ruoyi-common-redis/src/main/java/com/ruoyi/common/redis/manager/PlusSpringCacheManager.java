/**
 * Copyright (c) 2013-2021 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ruoyi.common.redis.manager;

import com.ruoyi.common.redis.utils.RedisUtils;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A {@link CacheManager} implementation
 * backed by Redisson instance.
 * <p>
 * 修改 RedissonSpringCacheManager 源码
 * 重写 cacheName 处理方法 支持多参数
 *
 * @author Nikita Koksharov
 *
 */
@SuppressWarnings("unchecked")
public class PlusSpringCacheManager implements CacheManager {

    private boolean dynamic = true;

    private boolean allowNullValues = true;

    private boolean transactionAware = true;

    Map<String, CacheConfig> configMap = new ConcurrentHashMap<>();
    ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<>();

    /**
     * Creates CacheManager supplied by Redisson instance
     */
    public PlusSpringCacheManager() {
    }


    /**
     * Defines possibility of storing {@code null} values.
     * <p>
     * Default is <code>true</code>
     *
     * @param allowNullValues stores if <code>true</code>
     */
    public void setAllowNullValues(boolean allowNullValues) {
        this.allowNullValues = allowNullValues;
    }

    /**
     * Defines if cache aware of Spring-managed transactions.
     * If {@code true} put/evict operations are executed only for successful transaction in after-commit phase.
     * <p>
     * Default is <code>false</code>
     *
     * @param transactionAware cache is transaction aware if <code>true</code>
     */
    public void setTransactionAware(boolean transactionAware) {
        this.transactionAware = transactionAware;
    }

    /**
     * Defines 'fixed' cache names.
     * A new cache instance will not be created in dynamic for non-defined names.
     * <p>
     * `null` parameter setups dynamic mode
     *
     * @param names of caches
     */
    public void setCacheNames(Collection<String> names) {
        if (names != null) {
            for (String name : names) {
                getCache(name);
            }
            dynamic = false;
        } else {
            dynamic = true;
        }
    }

    /**
     * Set cache config mapped by cache name
     *
     * @param config object
     */
    public void setConfig(Map<String, ? extends CacheConfig> config) {
        this.configMap = (Map<String, CacheConfig>) config;
    }

    protected CacheConfig createDefaultConfig() {
        return new CacheConfig();
    }

    @Override
    public Cache getCache(String name) {
        return Optional.ofNullable(instanceMap.get(name))
            .orElseGet(() -> dynamic ? createCache(name) : null);
    }

    private Cache createCache(String name) {
        CacheConfig config = configMap.computeIfAbsent(name, k -> createDefaultConfig());
        String[] array = StringUtils.delimitedListToStringArray(name, "#");
        name = array[0];
        config.setTTL(parseDuration(array, 1).toMillis());
        config.setMaxIdleTime(parseDuration(array, 2).toMillis());
        config.setMaxSize(parseInteger(array));
        RMapCache<Object, Object> mapCache = RedisUtils.getClient().getMapCache(name);
        if (config.getMaxSize() > 0) {
            mapCache.setMaxSize(config.getMaxSize());
        }
        Cache cache = new RedissonCache(mapCache, config, allowNullValues);
        if (transactionAware) {
            cache = new TransactionAwareCacheDecorator(cache);
        }
        Cache finalCache = cache;
        return instanceMap.computeIfAbsent(name, k -> finalCache);
    }

    private Duration parseDuration(String[] array, int index) {
        return Optional.ofNullable(index < array.length ? array[index] : null)
            .map(DurationStyle::detectAndParse)
            .orElse(Duration.ZERO);
    }

    private int parseInteger(String[] array) {
        return Optional.ofNullable(3 < array.length ? array[3] : null)
            .map(Integer::parseInt)
            .orElse(0);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(configMap.keySet());
    }


}
