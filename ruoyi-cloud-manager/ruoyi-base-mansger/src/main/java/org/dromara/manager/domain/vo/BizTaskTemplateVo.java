package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizTaskTemplate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任务模板视图对象 biz_task_template
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizTaskTemplate.class)
public class BizTaskTemplateVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "模板ID")
    private Long templateId;

    @ExcelProperty(value = "模板编码")
    private String templateCode;

    @ExcelProperty(value = "模板名称")
    private String templateName;

    private Long routeId;

    @ExcelProperty(value = "路线名称")
    private String routeName;

    @ExcelProperty(value = "任务说明")
    private String templateDesc;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "创建时间")
    private Date createTime;

    private String remark;

    private List<BizTaskTemplatePointVo> points;

}
