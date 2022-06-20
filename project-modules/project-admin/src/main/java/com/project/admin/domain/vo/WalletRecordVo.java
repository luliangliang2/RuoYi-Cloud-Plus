package com.project.admin.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 钱包交易记录视图对象 wallet_record
 *
 * @author project
 * @date 2022-06-20
 */
@Data
@ApiModel("钱包交易记录视图对象")
@ExcelIgnoreUnannotated
public class WalletRecordVo {

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
     * 支付方式
     */
    @ExcelProperty(value = "支付方式")
    @ApiModelProperty("支付方式")
    private Long fromId;

    /**
     * 接收方
     */
    @ExcelProperty(value = "接收方")
    @ApiModelProperty("接收方")
    private Long toId;

    /**
     * 交易类型
     */
    @ExcelProperty(value = "交易类型")
    @ApiModelProperty("交易类型")
    private Integer type;

    /**
     * 交易金额
     */
    @ExcelProperty(value = "交易金额")
    @ApiModelProperty("交易金额")
    private BigDecimal money;

    /**
     * 支付方式
     */
    @ExcelProperty(value = "支付方式")
    @ApiModelProperty("支付方式")
    private Integer payType;

    /**
     * 备注信息
     */
    @ExcelProperty(value = "备注信息")
    @ApiModelProperty("备注信息")
    private String remark;

    /**
     * 支付状态
     */
    @ExcelProperty(value = "支付状态")
    @ApiModelProperty("支付状态")
    private Integer payStatus;

    /**
     * 交易时间
     */
    @ExcelProperty(value = "交易时间")
    @ApiModelProperty("交易时间")
    private Date payTime;

    /**
     * 收款状态
     */
    @ExcelProperty(value = "收款状态")
    @ApiModelProperty("收款状态")
    private Integer fetchStatus;

    /**
     * 收款时间
     */
    @ExcelProperty(value = "收款时间")
    @ApiModelProperty("收款时间")
    private Date fetchTime;

    /**
     * 对账状态
     */
    @ExcelProperty(value = "对账状态")
    @ApiModelProperty("对账状态")
    private Integer checkStatus;

    /**
     * 对账时间
     */
    @ExcelProperty(value = "对账时间")
    @ApiModelProperty("对账时间")
    private Date checkTime;

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
