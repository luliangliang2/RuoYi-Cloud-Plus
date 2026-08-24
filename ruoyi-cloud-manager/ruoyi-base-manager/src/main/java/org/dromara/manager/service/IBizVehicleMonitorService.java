package org.dromara.manager.service;

import org.dromara.manager.domain.vo.BizVehicleMonitorTreeVo;

import java.util.List;

/**
 * 车辆监控Service接口
 *
 * @author LionLi
 * @date 2026-06-01
 */
public interface IBizVehicleMonitorService {

    /**
     * 查询车辆监控分类车辆树
     *
     * @param treeId 分类树ID
     * @param keyword 车辆VIN/车牌关键字
     * @return 分类车辆树
     */
    List<BizVehicleMonitorTreeVo> queryVehicleTree(Long treeId, String keyword);

}
