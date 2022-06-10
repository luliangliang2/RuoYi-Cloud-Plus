package com.project.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 人脉职业标签视图对象 user_occupation_label
 *
 * @author project
 * @date 2022-06-10
 */
@Data
@ApiModel("人脉职业标签视图对象")
@ExcelIgnoreUnannotated
public class UserOccupationLabelVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 代表当前用户;用户信息ID
     */
    @ExcelProperty(value = "代表当前用户;用户信息ID")
    @ApiModelProperty("代表当前用户;用户信息ID")
    private Long userInfoId;

    /**
     * 职业标签
     */
    @ExcelProperty(value = "职业标签")
    @ApiModelProperty("职业标签")
    private String occupationLabel;

    /**
     * 搜索值
     */
    @ExcelProperty(value = "搜索值")
    @ApiModelProperty("搜索值")
    private String searchValue;

    /**
     * 逻辑删除;0->未删除，1->删除
     */
    @ExcelProperty(value = "逻辑删除;0->未删除，1->删除")
    @ApiModelProperty("逻辑删除;0->未删除，1->删除")
    private Integer deleted;


}
