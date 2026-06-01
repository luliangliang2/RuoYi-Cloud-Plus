package org.dromara.manager.domain.vo;

import lombok.Data;
import org.dromara.manager.api.domain.vo.BizVehicleVo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 车辆监控树节点视图对象
 *
 * @author LionLi
 * @date 2026-06-01
 */
@Data
public class BizVehicleMonitorTreeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 前端树节点唯一KEY
     */
    private String key;

    /**
     * 节点标题
     */
    private String title;

    /**
     * 节点类型 category/vehicle
     */
    private String type;

    /**
     * 分类树ID
     */
    private Long treeId;

    /**
     * 分类节点ID
     */
    private Long nodeId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 车辆信息
     */
    private BizVehicleVo vehicle;

    /**
     * 子节点
     */
    private List<BizVehicleMonitorTreeVo> children = new ArrayList<>();

}
