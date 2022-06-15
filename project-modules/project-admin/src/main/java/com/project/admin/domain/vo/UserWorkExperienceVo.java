package com.project.admin.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 工作经历视图对象 user_work_experience
 *
 * @author huan.li
 * @date 2022-06-15
 */
@Data
@ApiModel("工作经历视图对象")
@ExcelIgnoreUnannotated
public class UserWorkExperienceVo {

    private static final long serialVersionUID = 1L;

    /**
     * 工作经历ID
     */
    @ExcelProperty(value = "工作经历ID")
    @ApiModelProperty("工作经历ID")
    private Long id;

    /**
     * 代表当前用户;用户信息ID
     */
    @ExcelProperty(value = "代表当前用户;用户信息ID")
    @ApiModelProperty("代表当前用户;用户信息ID")
    private Long userInfoId;

    /**
     * 公司
     */
    @ExcelProperty(value = "公司")
    @ApiModelProperty("公司")
    private String company;

    /**
     * 职位
     */
    @ExcelProperty(value = "职位")
    @ApiModelProperty("职位")
    private String position;

    /**
     * 入职时间
     */
    @ExcelProperty(value = "入职时间")
    @ApiModelProperty("入职时间")
    private Date entryTime;

    /**
     * 离职时间
     */
    @ExcelProperty(value = "离职时间")
    @ApiModelProperty("离职时间")
    private Date departureTime;

    /**
     * 就职时长
     */
    @ExcelProperty(value = "就职时长")
    @ApiModelProperty("就职时长")
    private Long lengthOfEmployment;

    /**
     * 说明介绍
     */
    @ExcelProperty(value = "说明介绍")
    @ApiModelProperty("说明介绍")
    private String introduction;

    /**
     * 搜索值
     */
    @ExcelProperty(value = "搜索值")
    @ApiModelProperty("搜索值")
    private String searchValue;

    /**
     * 逻辑删除;0->启用，1->停用
     */
    @ExcelProperty(value = "逻辑删除;0->启用，1->停用", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "deleted")
    @ApiModelProperty("逻辑删除;0->启用，1->停用")
    private Integer deleted;


}
