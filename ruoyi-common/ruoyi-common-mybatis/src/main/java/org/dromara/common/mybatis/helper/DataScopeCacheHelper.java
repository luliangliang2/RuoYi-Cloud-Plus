package org.dromara.common.mybatis.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * DataScope缓存工具类（独立ThreadLocal，仅存远程调用结果）
 */
public class DataScopeCacheHelper {
    // ThreadLocal实例必须初始化，但value初始为null（而非空Map）
    private static final ThreadLocal<Map<String, String>> DATA_SCOPE_CACHE = new ThreadLocal<>();

    // 存缓存：懒初始化Map，仅在真正存数据时创建
    public static void setCache(String methodName, Long param, String result) {
        if (param == null || result == null || methodName == null) {
            return;
        }
        // 懒加载：只有存数据时才初始化Map，避免无数据场景创建空Map
        Map<String, String> cacheMap = DATA_SCOPE_CACHE.get();
        if (cacheMap == null) {
            cacheMap = new HashMap<>();
            DATA_SCOPE_CACHE.set(cacheMap);
        }
        cacheMap.put(methodName + "_" + param, result);
    }

    // 取缓存：多层空值判断，避免NPE
    public static String getCache(String methodName, Long param) {
        if (param == null || methodName == null) {
            return null;
        }
        Map<String, String> cacheMap = DATA_SCOPE_CACHE.get();
        // 无Map/无数据时直接返回null
        if (cacheMap == null) {
            return null;
        }
        return cacheMap.get(methodName + "_" + param);
    }

    // 清缓存：彻底清理ThreadLocal，避免内存泄漏
    public static void clearCache() {
        // 先清空Map（如有），加速GC
        Map<String, String> cacheMap = DATA_SCOPE_CACHE.get();
        if (cacheMap != null) {
            cacheMap.clear();
        }
        // 移除ThreadLocal与当前线程的绑定（无论value是否为null）
        DATA_SCOPE_CACHE.remove();
    }
}
