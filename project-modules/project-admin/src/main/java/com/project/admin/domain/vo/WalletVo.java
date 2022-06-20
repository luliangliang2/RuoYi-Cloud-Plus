package com.project.admin.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户钱包视图对象 wallet
 *
 * @author project
 * @date 2022-06-19
 */
@Data
@ApiModel("用户钱包视图对象")
@ExcelIgnoreUnannotated
public class WalletVo {

    private static final long serialVersionUID = 1L;

    /**
     * 钱包id
     */
    @ExcelProperty(value = "钱包id")
    @ApiModelProperty("钱包id")
    private Long id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 总余额
     */
    @ExcelProperty(value = "总余额")
    @ApiModelProperty("总余额")
    private Long balance;

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
