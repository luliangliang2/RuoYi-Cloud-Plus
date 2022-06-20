package com.project.admin.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 钱包变动日志视图对象 wallet_log
 *
 * @author project
 * @date 2022-06-20
 */
@Data
@ApiModel("钱包变动日志视图对象")
@ExcelIgnoreUnannotated
public class WalletLogVo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @ExcelProperty(value = "自增ID")
    @ApiModelProperty("自增ID")
    private Long id;

    /**
     * 交易流水
     */
    @ExcelProperty(value = "交易流水")
    @ApiModelProperty("交易流水")
    private String number;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 变动金额
     */
    @ExcelProperty(value = "变动金额")
    @ApiModelProperty("变动金额")
    private Long beforeMoney;

    /**
     * 变动金额
     */
    @ExcelProperty(value = "变动金额")
    @ApiModelProperty("变动金额")
    private Long afterMoney;

    /**
     * 业务类型
     */
    @ExcelProperty(value = "业务类型")
    @ApiModelProperty("业务类型")
    private Integer targetType;

    /**
     * 目标id
     */
    @ExcelProperty(value = "目标id")
    @ApiModelProperty("目标id")
    private Long targetId;

    /**
     * 处理状态
     */
    @ExcelProperty(value = "处理状态")
    @ApiModelProperty("处理状态")
    private Integer status;

    /**
     * 处理结果
     */
    @ExcelProperty(value = "处理结果")
    @ApiModelProperty("处理结果")
    private Integer resultType;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 启用状态
     */
    @ExcelProperty(value = "启用状态")
    @ApiModelProperty("启用状态")
    private Integer deleted;

    /**
     * 搜索值
     */
    @ExcelProperty(value = "搜索值")
    @ApiModelProperty("搜索值")
    private String searchValue;


}
