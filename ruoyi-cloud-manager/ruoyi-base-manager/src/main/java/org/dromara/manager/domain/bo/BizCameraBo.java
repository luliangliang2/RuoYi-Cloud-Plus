package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizCamera;

import java.math.BigDecimal;
import java.util.List;

/**
 * 上装相机业务对象 biz_camera
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizCamera.class, reverseConvertGenerate = false)
public class BizCameraBo extends BaseEntity {

    /**
     * 相机ID
     */
    @NotNull(message = "相机ID不能为空", groups = { EditGroup.class })
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
     * 分类节点ID集合
     */
    private List<Long> categoryNodeIds;

    /**
     * 相机编码
     */
    @NotBlank(message = "相机编码不能为空")
    @Size(max = 64, message = "相机编码长度不能超过{max}个字符")
    private String cameraCode;

    /**
     * 相机名称
     */
    @NotBlank(message = "相机名称不能为空")
    @Size(max = 100, message = "相机名称长度不能超过{max}个字符")
    private String cameraName;

    /**
     * 设备SN号
     */
    @Size(max = 100, message = "设备SN号长度不能超过{max}个字符")
    private String sn;

    /**
     * 光角度数
     */
    @DecimalMin(value = "0", message = "光角度数不能小于0")
    private BigDecimal viewAngle;

    /**
     * 厂商
     */
    @Size(max = 100, message = "厂商长度不能超过{max}个字符")
    private String manufacturer;

    /**
     * 型号
     */
    @Size(max = 100, message = "型号长度不能超过{max}个字符")
    private String modelName;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过{max}个字符")
    private String remark;

}
