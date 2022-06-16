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
 * @date 2022-06-16
 */
@Data
@ApiModel("工作经历视图对象")
@ExcelIgnoreUnannotated
public class UserWorkExperienceVo {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @ExcelProperty(value = "序号")
    @ApiModelProperty("序号")
    private Long id;

    /**
     * 用户序号
     */
    @ExcelProperty(value = "用户序号")
    @ApiModelProperty("用户序号")
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
     * 启用状态
     */
    @ExcelProperty(value = "启用状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "deleted")
    @ApiModelProperty("启用状态")
    private Integer deleted;


}
