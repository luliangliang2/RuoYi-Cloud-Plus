package com.project.admin.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 好友分组视图对象 user_friend_group
 *
 * @author huan.li
 * @date 2022-06-16
 */
@Data
@ApiModel("好友分组视图对象")
@ExcelIgnoreUnannotated
public class UserFriendGroupVo {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @ExcelProperty(value = "序号")
    @ApiModelProperty("序号")
    private Long id;

    /**
     * 分组名称
     */
    @ExcelProperty(value = "分组名称")
    @ApiModelProperty("分组名称")
    private String groupName;

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
