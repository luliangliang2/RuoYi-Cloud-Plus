package org.dromara.magicapi.cache;

import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * 车辆缓存刷出回调。
 *
 * @author ruoyi
 */
@FunctionalInterface
public interface MetricFlushHandler {

    /**
     * 刷出车辆缓存数据。
     *
     * @param vin 车辆 VIN
     * @param metrics 车辆数据列表
     * @param reason 刷出原因 batch/timeout/manual
     */
    void callback(String vin, List<JSONObject> metrics, String reason);
}
