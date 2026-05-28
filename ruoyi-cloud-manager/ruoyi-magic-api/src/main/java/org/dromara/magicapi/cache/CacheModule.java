package org.dromara.magicapi.cache;

import cn.hutool.json.JSONObject;
import org.ssssssss.magicapi.core.annotation.MagicModule;
import org.ssssssss.magicapi.modules.DynamicModule;
import org.ssssssss.script.MagicScriptContext;
import org.ssssssss.script.annotation.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Magic API 车辆缓存工具模块。
 *
 * <p>脚本中使用：import cacheModule;</p>
 *
 * @author ruoyi
 */
@Component
@MagicModule("cacheModule")
public class CacheModule implements DynamicModule<CacheModule> {

    private final MetricCacheManager metricCacheManager;

    public CacheModule(MetricCacheManager metricCacheManager) {
        this.metricCacheManager = metricCacheManager;
    }

    @Override
    public CacheModule getDynamicModule(MagicScriptContext context) {
        return this;
    }

    @Comment("配置车辆缓存，回调函数如：(vin, metrics, reason) => {...}")
    public void configure(
        @Comment(name = "batchSize", value = "单车满批次条数") Integer batchSize,
        @Comment(name = "expireMillis", value = "单车无新数据超时毫秒数") Long expireMillis,
        @Comment(name = "flushHandler", value = "刷出回调") MetricFlushHandler flushHandler) {
    	metricCacheManager.configure(batchSize, expireMillis, flushHandler);
    }

    @Comment("设置车辆缓存刷出回调，回调函数如：(vin, metrics, reason) => {...}")
    public void onFlush(@Comment(name = "flushHandler", value = "刷出回调") MetricFlushHandler flushHandler) {
    	metricCacheManager.setFlushHandler(flushHandler);
    }

    @Comment("添加一条车辆数据")
    public int add(
        @Comment(name = "vin", value = "车辆VIN") String vin,
        @Comment(name = "metric", value = "车辆JSON数据") JSONObject metric) {
        return metricCacheManager.add(vin, metric);
    }

    @Comment("添加一条车辆数据")
    public int addMap(
        @Comment(name = "vin", value = "车辆VIN") String vin,
        @Comment(name = "metric", value = "车辆Map数据") Map<String, ?> metric) {
        return metricCacheManager.add(vin, metric);
    }

    @Comment("添加一条车辆数据")
    public int addJson(
        @Comment(name = "vin", value = "车辆VIN") String vin,
        @Comment(name = "metricJson", value = "车辆JSON字符串") String metricJson) {
        return metricCacheManager.addJson(vin, metricJson);
    }

    @Comment("添加一条车辆数据，并同时设置刷出回调")
    public int add(
        @Comment(name = "vin", value = "车辆VIN") String vin,
        @Comment(name = "metric", value = "车辆JSON数据") JSONObject metric,
        @Comment(name = "flushHandler", value = "刷出回调") MetricFlushHandler flushHandler) {
    	metricCacheManager.setFlushHandler(flushHandler);
        return metricCacheManager.add(vin, metric);
    }

    @Comment("手动刷出指定车辆数据")
    public int flush(@Comment(name = "vin", value = "车辆VIN") String vin) {
        return metricCacheManager.flush(vin);
    }

    @Comment("手动刷出全部车辆数据")
    public int flushAll() {
        return metricCacheManager.flushAll();
    }

    @Comment("取出并移除指定车辆数据，不触发刷出回调")
    public List<JSONObject> drain(@Comment(name = "vin", value = "车辆VIN") String vin) {
        return metricCacheManager.drain(vin);
    }

    @Comment("查看指定车辆缓存快照")
    public List<JSONObject> snapshot(@Comment(name = "vin", value = "车辆VIN") String vin) {
        return metricCacheManager.snapshot(vin);
    }

    @Comment("清空指定车辆缓存")
    public int clear(@Comment(name = "vin", value = "车辆VIN") String vin) {
        return metricCacheManager.clear(vin);
    }

    @Comment("清空全部车辆缓存")
    public int clearAll() {
        return metricCacheManager.clearAll();
    }

    @Comment("获取指定车辆缓存条数")
    public int size(@Comment(name = "vin", value = "车辆VIN") String vin) {
        return metricCacheManager.size(vin);
    }

    @Comment("获取当前缓存车辆数")
    public int vehicleCount() {
        return metricCacheManager.vehicleCount();
    }

    @Comment("获取车辆缓存状态")
    public Map<String, Object> stats() {
        return metricCacheManager.stats();
    }
}
