package com.project.commodity.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 订单详情视图对象 order_detail
 *
 * @author huan.li
 * @date 2022-06-10
 */
@Data
@ApiModel("订单详情视图对象")
@ExcelIgnoreUnannotated
public class OrderDetailVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 订单id
     */
    @ExcelProperty(value = "订单id")
    @ApiModelProperty("订单id")
    private String orderId;

    /**
     * 订单编号
     */
    @ExcelProperty(value = "订单编号")
    @ApiModelProperty("订单编号")
    private String orderNum;

    /**
     * 商品id
     */
    @ExcelProperty(value = "商品id")
    @ApiModelProperty("商品id")
    private Long productId;

    /**
     * 商品图片
     */
    @ExcelProperty(value = "商品图片")
    @ApiModelProperty("商品图片")
    private String productPic;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    @ApiModelProperty("商品名称")
    private String productName;

    /**
     * 商品价格
     */
    @ExcelProperty(value = "商品价格")
    @ApiModelProperty("商品价格")
    private Long productPrice;

    /**
     * 商品数量
     */
    @ExcelProperty(value = "商品数量")
    @ApiModelProperty("商品数量")
    private Long productQuantity;

    /**
     * 商品类型id
     */
    @ExcelProperty(value = "商品类型id")
    @ApiModelProperty("商品类型id")
    private Long productCategoryId;

    /**
     * 订单总金额
     */
    @ExcelProperty(value = "订单总金额")
    @ApiModelProperty("订单总金额")
    private Long totalPrice;

    /**
     * 逻辑删除;0未删除1已删除
     */
    @ExcelProperty(value = "逻辑删除;0未删除1已删除")
    @ApiModelProperty("逻辑删除;0未删除1已删除")
    private Integer deleted;


}
