package com.project.admin.domain.bo;

import com.project.common.core.validate.AddGroup;
import com.project.common.core.validate.EditGroup;
import com.project.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;


/**
 * 用户信息业务对象 user_info
 *
 * @author huan.li
 * @date 2022-06-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户信息业务对象")
public class UserInfoBo extends BaseEntity {

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号", required = true)
    @NotNull(message = "序号不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank(message = "姓名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", required = true)
    @NotBlank(message = "昵称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String nick;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码", required = true)
    @NotBlank(message = "手机号码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String phone;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像", required = true)
    @NotBlank(message = "头像不能为空", groups = {AddGroup.class, EditGroup.class})
    private String avatar;

    /**
     * 影响力
     */
    @ApiModelProperty(value = "影响力", required = true)
    @NotNull(message = "影响力不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long effectCount;

    /**
     * 访客数量
     */
    @ApiModelProperty(value = "访客数量", required = true)
    @NotNull(message = "访客数量不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long visitorCount;

    /**
     * 公司
     */
    @ApiModelProperty(value = "公司", required = true)
    @NotBlank(message = "公司不能为空", groups = {AddGroup.class, EditGroup.class})
    private String company;

    /**
     * 职位
     */
    @ApiModelProperty(value = "职位", required = true)
    @NotBlank(message = "职位不能为空", groups = {AddGroup.class, EditGroup.class})
    private String position;

    /**
     * 自我介绍
     */
    @ApiModelProperty(value = "自我介绍", required = true)
    @NotBlank(message = "自我介绍不能为空", groups = {AddGroup.class, EditGroup.class})
    private String selfIntroduction;

    /**
     * 职业方向
     */
    @ApiModelProperty(value = "职业方向", required = true)
    @NotBlank(message = "职业方向不能为空", groups = {AddGroup.class, EditGroup.class})
    private String careerDirection;

    /**
     * 所在位置
     */
    @ApiModelProperty(value = "所在位置", required = true)
    @NotBlank(message = "所在位置不能为空", groups = {AddGroup.class, EditGroup.class})
    private String location;

    /**
     * 家乡
     */
    @ApiModelProperty(value = "家乡", required = true)
    @NotBlank(message = "家乡不能为空", groups = {AddGroup.class, EditGroup.class})
    private String hometown;

    /**
     * 星座
     */
    @ApiModelProperty(value = "星座", required = true)
    @NotNull(message = "星座不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer constellation;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", required = true)
    @NotBlank(message = "邮箱不能为空", groups = {AddGroup.class, EditGroup.class})
    private String email;

    /**
     * 搜索值
     */
    @ApiModelProperty(value = "搜索值", required = true)
    @NotBlank(message = "搜索值不能为空", groups = {AddGroup.class, EditGroup.class})
    private String searchValue;

    /**
     * 启用状态
     */
    @ApiModelProperty(value = "启用状态", required = true)
    @NotNull(message = "启用状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer deleted;


}
