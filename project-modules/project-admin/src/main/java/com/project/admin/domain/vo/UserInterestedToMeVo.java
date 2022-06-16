package com.project.admin.domain.vo;

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
 * @date 2022-06-16
 */
@Data
@ApiModel("对我感兴趣视图对象")
@ExcelIgnoreUnannotated
public class UserInterestedToMeVo {

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
     * 好友序号
     */
    @ExcelProperty(value = "好友序号")
    @ApiModelProperty("好友序号")
    private Long friendInfoId;

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
