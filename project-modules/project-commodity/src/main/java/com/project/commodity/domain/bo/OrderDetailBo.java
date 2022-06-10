package com.project.commodity.domain.bo;

import com.project.common.core.validate.AddGroup;
import com.project.common.core.validate.EditGroup;
import com.project.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;


/**
 * 订单详情业务对象 order_detail
 *
 * @author huan.li
 * @date 2022-06-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("订单详情业务对象")
public class OrderDetailBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id", required = true)
    @NotBlank(message = "订单id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String orderId;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号", required = true)
    @NotBlank(message = "订单编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String orderNum;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long productId;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片", required = true)
    @NotBlank(message = "商品图片不能为空", groups = {AddGroup.class, EditGroup.class})
    private String productPic;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank(message = "商品名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String productName;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格", required = true)
    @NotNull(message = "商品价格不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long productPrice;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量", required = true)
    @NotNull(message = "商品数量不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long productQuantity;

    /**
     * 商品类型id
     */
    @ApiModelProperty(value = "商品类型id", required = true)
    @NotNull(message = "商品类型id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long productCategoryId;

    /**
     * 订单总金额
     */
    @ApiModelProperty(value = "订单总金额", required = true)
    @NotNull(message = "订单总金额不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long totalPrice;

    /**
     * 逻辑删除;0未删除1已删除
     */
    @ApiModelProperty(value = "逻辑删除;0未删除1已删除", required = true)
    @NotNull(message = "逻辑删除;0未删除1已删除不能为空", groups = {EditGroup.class})
    private Integer deleted;


}
