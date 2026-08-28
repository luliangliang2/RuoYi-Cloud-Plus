package org.dromara.magicapi.cache;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 多车辆上报数据缓存容器。
 *
 * <p>按 VIN 分桶缓存数据，支持满批次刷出、超时刷出和手动刷出。</p>
 *
 * @author ruoyi
 */
@Component
public class MetricCacheManager implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(MetricCacheManager.class);

    private static final int DEFAULT_BATCH_SIZE = 500;

    private static final long DEFAULT_EXPIRE_MILLIS = 100L;

    private static final long CLEAN_INTERVAL_MILLIS = 100L;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final AtomicReference<MetricFlushHandler> flushHandler = new AtomicReference<>();

    private final ScheduledExecutorService cleanExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "magic-vehicle-cache-cleaner");
        thread.setDaemon(true);
        return thread;
    });

    private final ExecutorService flushExecutor = Executors.newFixedThreadPool(2, runnable -> {
        Thread thread = new Thread(runnable, "magic-vehicle-cache-flusher");
        thread.setDaemon(true);
        return thread;
    });

    private volatile int batchSize = DEFAULT_BATCH_SIZE;

    private volatile long expireMillis = DEFAULT_EXPIRE_MILLIS;

    public MetricCacheManager() {
        cleanExecutor.scheduleAtFixedRate(this::flushExpiredQuietly,
            CLEAN_INTERVAL_MILLIS, CLEAN_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    }

    /**
     * 配置缓存参数和刷盘回调。
     */
    public void configure(Integer batchSize, Long expireMillis, MetricFlushHandler handler) {
        if (batchSize != null && batchSize > 0) {
            this.batchSize = batchSize;
        }
        if (expireMillis != null && expireMillis > 0) {
            this.expireMillis = expireMillis;
        }
        if (handler != null) {
            this.flushHandler.set(handler);
        }
    }

    /**
     * 设置刷盘回调。
     */
    public void setFlushHandler(MetricFlushHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("flushHandler must not be null");
        }
        this.flushHandler.set(handler);
    }

    /**
     * 添加一条车辆数据。
     *
     * @return 当前车辆缓存条数；如果本次触发满批次刷出，则返回 0
     */
    public int add(String vin, JSONObject metric) {
        if (StrUtil.isBlank(vin)) {
            throw new IllegalArgumentException("vin must not be blank");
        }
        if (metric == null) {
            throw new IllegalArgumentException("metric must not be null");
        }

        long now = System.currentTimeMillis();
        Bucket bucket = buckets.computeIfAbsent(vin, key -> new Bucket());
        List<JSONObject> batch = null;
        int currentSize;
        synchronized (bucket) {
            bucket.metrics.add(metric);
            bucket.lastWriteTime = now;
            currentSize = bucket.metrics.size();
            if (currentSize >= batchSize && flushHandler.get() != null) {
                batch = drainLocked(bucket);
                currentSize = 0;
            }
        }
        if (batch != null) {
        	buckets.remove(vin, bucket);
            flushAsync(vin, batch, "batch");
        }
        return currentSize;
    }

    /**
     * 添加一条车辆数据。
     */
    public int add(String vin, Map<String, ?> metric) {
        if (metric == null) {
            throw new IllegalArgumentException("metric must not be null");
        }
        return add(vin, new JSONObject(metric));
    }

    /**
     * 添加一条车辆数据。
     */
    public int addJson(String vin, String metricJson) {
        if (StrUtil.isBlank(metricJson)) {
            throw new IllegalArgumentException("metricJson must not be blank");
        }
        return add(vin, JSONUtil.parseObj(metricJson));
    }

    /**
     * 手动刷出指定车辆数据。
     */
    public int flush(String vin) {
        List<JSONObject> batch = drain(vin);
        if (batch.isEmpty()) {
            return 0;
        }
        flushAsync(vin, batch, "manual");
        return batch.size();
    }

    /**
     * 手动刷出所有车辆数据。
     */
    public int flushAll() {
        AtomicInteger count = new AtomicInteger();
        for (String vin : new ArrayList<>(buckets.keySet())) {
            count.addAndGet(flush(vin));
        }
        return count.get();
    }

    /**
     * 取出并移除指定车辆数据，不触发刷盘回调。
     */
    public List<JSONObject> drain(String vin) {
        if (StrUtil.isBlank(vin)) {
            return List.of();
        }
        Bucket bucket = buckets.remove(vin);
        if (bucket == null) {
            return List.of();
        }
        synchronized (bucket) {
            return drainLocked(bucket);
        }
    }

    /**
     * 查看指定车辆缓存快照。
     */
    public List<JSONObject> snapshot(String vin) {
        if (StrUtil.isBlank(vin)) {
            return List.of();
        }
        Bucket bucket = buckets.get(vin);
        if (bucket == null) {
            return List.of();
        }
        synchronized (bucket) {
            return new ArrayList<>(bucket.metrics);
        }
    }

    /**
     * 清空指定车辆缓存。
     */
    public int clear(String vin) {
        return drain(vin).size();
    }

    /**
     * 清空全部缓存。
     */
    public int clearAll() {
        AtomicInteger count = new AtomicInteger();
        for (String vin : new ArrayList<>(buckets.keySet())) {
            count.addAndGet(clear(vin));
        }
        return count.get();
    }

    /**
     * 当前车辆缓存条数。
     */
    public int size(String vin) {
        if (StrUtil.isBlank(vin)) {
            return 0;
        }
        Bucket bucket = buckets.get(vin);
        if (bucket == null) {
            return 0;
        }
        synchronized (bucket) {
            return bucket.metrics.size();
        }
    }

    /**
     * 当前缓存中的车辆数。
     */
    public int vehicleCount() {
        return buckets.size();
    }

    /**
     * 缓存状态。
     */
    public Map<String, Object> stats() {
        Map<String, Integer> vehicleSizes = new LinkedHashMap<>();
        for (Map.Entry<String, Bucket> entry : buckets.entrySet()) {
            Bucket bucket = entry.getValue();
            synchronized (bucket) {
                vehicleSizes.put(entry.getKey(), bucket.metrics.size());
            }
        }
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("batchSize", batchSize);
        stats.put("expireMillis", expireMillis);
        stats.put("vehicleCount", buckets.size());
        stats.put("vehicleSizes", vehicleSizes);
        stats.put("flushHandlerReady", flushHandler.get() != null);
        return stats;
    }

    private void flushExpiredQuietly() {
        try {
            flushExpired();
        } catch (Exception e) {
            log.error("flush expired vehicle metrics failed", e);
        }
    }

    private void flushExpired() {
        if (flushHandler.get() == null) {
            return;
        }
        long now = System.currentTimeMillis();
        for (Map.Entry<String, Bucket> entry : buckets.entrySet()) {
            String vin = entry.getKey();
            Bucket bucket = entry.getValue();
            List<JSONObject> batch = null;
            synchronized (bucket) {
                if (!bucket.metrics.isEmpty() && now - bucket.lastWriteTime >= expireMillis) {
                    batch = drainLocked(bucket);
                }
            }
            if (batch != null) {
            	buckets.remove(vin, bucket);
                flushAsync(vin, batch, "timeout");
            }
        }
    }

    private void flushAsync(String vin, List<JSONObject> batch, String reason) {
        MetricFlushHandler handler = flushHandler.get();
        if (handler == null) {
            requeue(vin, batch);
            log.warn("vehicle metric flush handler is not configured, vin={}, size={}, reason={}", vin, batch.size(), reason);
            return;
        }
        flushExecutor.execute(() -> {
            try {
                handler.callback(vin, batch, reason);
            } catch (Exception e) {
                requeue(vin, batch);
                log.error("flush vehicle metrics failed, vin={}, size={}, reason={}", vin, batch.size(), reason, e);
            }
        });
    }

    private void requeue(String vin, List<JSONObject> batch) {
        if (batch == null || batch.isEmpty()) {
            return;
        }
        Bucket bucket = buckets.computeIfAbsent(vin, key -> new Bucket());
        synchronized (bucket) {
            List<JSONObject> retry = new ArrayList<>(batch);
            retry.addAll(bucket.metrics);
            bucket.metrics.clear();
            bucket.metrics.addAll(retry);
            bucket.lastWriteTime = System.currentTimeMillis();
        }
    }

    private List<JSONObject> drainLocked(Bucket bucket) {
        List<JSONObject> batch = new ArrayList<>(bucket.metrics);
        bucket.metrics.clear();
        return batch;
    }

    @Override
    public void destroy() {
        cleanExecutor.shutdownNow();
        flushAll();
        flushExecutor.shutdown();
        try {
            if (!flushExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                flushExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            flushExecutor.shutdownNow();
        }
    }

    private static class Bucket {

        private final List<JSONObject> metrics = new ArrayList<>();

        private long lastWriteTime = System.currentTimeMillis();
    }
}
