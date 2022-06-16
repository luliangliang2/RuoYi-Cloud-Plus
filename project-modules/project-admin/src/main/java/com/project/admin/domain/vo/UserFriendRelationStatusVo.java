package com.project.admin.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 关系状态视图对象 user_friend_relation_status
 *
 * @author huan.li
 * @date 2022-06-16
 */
@Data
@ApiModel("关系状态视图对象")
@ExcelIgnoreUnannotated
public class UserFriendRelationStatusVo {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @ExcelProperty(value = "序号")
    @ApiModelProperty("序号")
    private Long id;

    /**
     * 关系状态
     */
    @ExcelProperty(value = "关系状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "approved_status")
    @ApiModelProperty("关系状态")
    private Integer approvedStatus;

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
