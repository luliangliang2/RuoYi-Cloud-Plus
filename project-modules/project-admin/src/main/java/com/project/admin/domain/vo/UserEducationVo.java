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
 * 学历视图对象 user_education
 *
 * @author huan.li
 * @date 2022-06-16
 */
@Data
@ApiModel("学历视图对象")
@ExcelIgnoreUnannotated
public class UserEducationVo {

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
     * 学校名称
     */
    @ExcelProperty(value = "学校名称")
    @ApiModelProperty("学校名称")
    private String name;

    /**
     * 入学时间
     */
    @ExcelProperty(value = "入学时间")
    @ApiModelProperty("入学时间")
    private Date admissionTime;

    /**
     * 毕业时间
     */
    @ExcelProperty(value = "毕业时间")
    @ApiModelProperty("毕业时间")
    private Date graduationTime;

    /**
     * 专业
     */
    @ExcelProperty(value = "专业")
    @ApiModelProperty("专业")
    private String major;

    /**
     * 文化程度
     */
    @ExcelProperty(value = "文化程度", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "grade_level")
    @ApiModelProperty("文化程度")
    private Integer geadeLevel;

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
