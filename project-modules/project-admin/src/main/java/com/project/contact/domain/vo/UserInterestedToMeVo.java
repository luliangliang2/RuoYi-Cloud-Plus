package com.project.contact.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 对我感兴趣视图对象 user_interested_to_me
 *
 * @author huan.li
 * @date 2022-06-10
 */
@Data
@ApiModel("对我感兴趣视图对象")
@ExcelIgnoreUnannotated
public class UserInterestedToMeVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 当前用户
     */
    @ExcelProperty(value = "当前用户")
    @ApiModelProperty("当前用户")
    private Long userInfoId;

    /**
     * 人脉用户
     */
    @ExcelProperty(value = "人脉用户")
    @ApiModelProperty("人脉用户")
    private Long contactInfoId;

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
