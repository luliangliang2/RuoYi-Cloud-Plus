package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizRadar;

import java.math.BigDecimal;
import java.util.List;

/**
 * 上装雷达业务对象 biz_radar
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizRadar.class, reverseConvertGenerate = false)
public class BizRadarBo extends BaseEntity {

    /**
     * 雷达ID
     */
    @NotNull(message = "雷达ID不能为空", groups = { EditGroup.class })
    private Long radarId;

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
     * 雷达编码
     */
    @NotBlank(message = "雷达编码不能为空")
    @Size(max = 64, message = "雷达编码长度不能超过{max}个字符")
    private String radarCode;

    /**
     * 雷达名称
     */
    @NotBlank(message = "雷达名称不能为空")
    @Size(max = 100, message = "雷达名称长度不能超过{max}个字符")
    private String radarName;

    /**
     * 设备SN号
     */
    @Size(max = 100, message = "设备SN号长度不能超过{max}个字符")
    private String sn;

    /**
     * 雷达线数
     */
    @Min(value = 1, message = "雷达线数不能小于1")
    private Integer lineCount;

    /**
     * 探测范围（米）
     */
    @DecimalMin(value = "0", message = "探测范围不能小于0")
    private BigDecimal detectionRange;

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
