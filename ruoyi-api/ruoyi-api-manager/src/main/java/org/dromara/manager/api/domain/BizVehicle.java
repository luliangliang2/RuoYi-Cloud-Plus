package org.dromara.manager.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import org.dromara.common.tenant.core.TenantEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

/**
 * 车辆管理对象 biz_vehicle
 *
 * @author LionLi
 * @date 2026-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_vehicle")
public class BizVehicle extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 分类树ID
     */
    private Long treeId;

    /**
     * 分类节点ID
     */
    private Long categoryNodeId;

    /**
     * 分类节点ID集合（查询使用）
     */
    @TableField(exist = false)
    private List<Long> categoryNodeIds;

    /**
     * vin码
     */
    private String vin;

    /**
     * 车牌
     */
    private String plateNo;

    /**
     * 车辆品牌
     */
    private String brand;

    /**
     * 删除标志
     */
    @TableLogic
    private Long delFlag;


}
