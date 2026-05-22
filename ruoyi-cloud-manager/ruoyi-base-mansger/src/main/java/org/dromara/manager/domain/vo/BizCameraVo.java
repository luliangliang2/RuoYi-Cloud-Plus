package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizCamera;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 上装相机视图对象 biz_camera
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizCamera.class)
public class BizCameraVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 相机ID
     */
    @ExcelProperty(value = "相机ID")
    private Long cameraId;

    /**
     * 分类树ID
     */
    private Long treeId;

    /**
     * 分类节点ID
     */
    private Long categoryNodeId;

    /**
     * 分类节点名称
     */
    @ExcelProperty(value = "分类")
    private String categoryNodeName;

    /**
     * 相机编码
     */
    @ExcelProperty(value = "相机编码")
    private String cameraCode;

    /**
     * 相机名称
     */
    @ExcelProperty(value = "相机名称")
    private String cameraName;

    /**
     * 设备SN号
     */
    @ExcelProperty(value = "设备SN号")
    private String sn;

    /**
     * 光角度数
     */
    @ExcelProperty(value = "光角度数")
    private BigDecimal viewAngle;

    /**
     * 厂商
     */
    @ExcelProperty(value = "厂商")
    private String manufacturer;

    /**
     * 型号
     */
    @ExcelProperty(value = "型号")
    private String modelName;

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
