package com.project.admin.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 好友关系视图对象 user_friend_relation
 *
 * @author huan.li
 * @date 2022-06-16
 */
@Data
@ApiModel("好友关系视图对象")
@ExcelIgnoreUnannotated
public class UserFriendRelationVo {

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
     * 分组序号
     */
    @ExcelProperty(value = "分组序号")
    @ApiModelProperty("分组序号")
    private Long userFriendGroupId;

    /**
     * 关系状态序号
     */
    @ExcelProperty(value = "关系状态序号")
    @ApiModelProperty("关系状态序号")
    private Long userFriendRelationStatusId;

    /**
     * 交换状态
     */
    @ExcelProperty(value = "交换状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "exchange_phone_status")
    @ApiModelProperty("交换状态")
    private Integer exchangePhoneStatus;

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
