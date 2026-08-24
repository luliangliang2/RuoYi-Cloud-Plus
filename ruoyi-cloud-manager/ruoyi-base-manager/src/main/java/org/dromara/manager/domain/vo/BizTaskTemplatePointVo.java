package org.dromara.manager.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 任务模板点位编排视图对象
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Data
public class BizTaskTemplatePointVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long templatePointId;
    private Long templateId;
    private Long routeId;
    private Long pointId;
    private String pointName;
    private BigDecimal gcj02Lng;
    private BigDecimal gcj02Lat;
    private BigDecimal bd09Lng;
    private BigDecimal bd09Lat;
    private BigDecimal wgs84Lng;
    private BigDecimal wgs84Lat;
    private Integer sequence;
    private String requiredFlag;
    private String remark;
    private List<BizTaskTemplateActionVo> actions;

}
