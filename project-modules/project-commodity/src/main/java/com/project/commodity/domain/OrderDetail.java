package com.project.commodity.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 订单详情对象 order_detail
 *
 * @author huan.li
 * @date 2022-06-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_detail")
public class OrderDetail extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 订单编号
     */
    private String orderNum;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 商品图片
     */
    private String productPic;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品价格
     */
    private Long productPrice;
    /**
     * 商品数量
     */
    private Long productQuantity;
    /**
     * 商品类型id
     */
    private Long productCategoryId;
    /**
     * 订单总金额
     */
    private Long totalPrice;
    /**
     * 逻辑删除;0未删除1已删除
     */
    private Integer deleted;

}
