package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizSimCard;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * SIM卡视图对象 biz_sim_card
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizSimCard.class)
public class BizSimCardVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * SIM卡ID
     */
    @ExcelProperty(value = "SIM卡ID")
    private Long simId;

    /**
     * 分类树ID
     */
    private Long treeId;

    /**
     * 分类节点ID
     */
    private Long categoryNodeId;

    /**
     * 分类节点ID集合
     */
    private List<Long> categoryNodeIds;

    /**
     * 分类节点名称
     */
    @ExcelProperty(value = "分类")
    private String categoryNodeName;

    /**
     * IMEI
     */
    @ExcelProperty(value = "IMEI")
    private String imei;

    /**
     * ICCID
     */
    @ExcelProperty(value = "ICCID")
    private String iccid;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    private String phoneNumber;

    /**
     * 月套餐流量（GB）
     */
    @ExcelProperty(value = "月套餐流量（GB）")
    private BigDecimal monthlyDataQuota;

    /**
     * 开卡时间
     */
    @ExcelProperty(value = "开卡时间")
    private Date activationTime;

    /**
     * 到期时间
     */
    @ExcelProperty(value = "到期时间")
    private Date expireTime;

    /**
     * 当前用量（GB）
     */
    @ExcelProperty(value = "当前用量（GB）")
    private BigDecimal currentDataUsage;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 是否已绑定车辆
     */
    private Boolean bound;

    /**
     * 绑定车辆ID
     */
    private Long boundVehicleId;

    /**
     * 绑定车辆车牌
     */
    private String boundPlateNo;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

}
