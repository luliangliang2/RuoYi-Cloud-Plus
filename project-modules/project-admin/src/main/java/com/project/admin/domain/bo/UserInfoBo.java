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
 * 用户详情业务对象 user_info
 *
 * @author huan.li
 * @date 2022-06-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户详情业务对象")
public class UserInfoBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户登录id
     */
    @ApiModelProperty(value = "用户登录id", required = true)
    @NotNull(message = "用户登录id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userLoginId;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像", required = true)
    @NotBlank(message = "头像不能为空", groups = { AddGroup.class, EditGroup.class })
    private String headUrl;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称", required = true)
    @NotBlank(message = "用户昵称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String nickName;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名", required = true)
    @NotBlank(message = "真实姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String realName;

    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证号码", required = true)
    @NotBlank(message = "身份证号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String idCardNo;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话", required = true)
    @NotBlank(message = "电话不能为空", groups = { AddGroup.class, EditGroup.class })
    private String phone;

    /**
     * 学校
     */
    @ApiModelProperty(value = "学校", required = true)
    @NotBlank(message = "学校不能为空", groups = { AddGroup.class, EditGroup.class })
    private String school;

    /**
     * 学院
     */
    @ApiModelProperty(value = "学院", required = true)
    @NotBlank(message = "学院不能为空", groups = { AddGroup.class, EditGroup.class })
    private String college;

    /**
     * 年级
     */
    @ApiModelProperty(value = "年级", required = true)
    @NotBlank(message = "年级不能为空", groups = { AddGroup.class, EditGroup.class })
    private String grade;

    /**
     * 专业
     */
    @ApiModelProperty(value = "专业", required = true)
    @NotBlank(message = "专业不能为空", groups = { AddGroup.class, EditGroup.class })
    private String major;

    /**
     * 个性签名
     */
    @ApiModelProperty(value = "个性签名", required = true)
    @NotBlank(message = "个性签名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String personalSignature;

    /**
     * 家乡
     */
    @ApiModelProperty(value = "家乡", required = true)
    @NotBlank(message = "家乡不能为空", groups = { AddGroup.class, EditGroup.class })
    private String hometown;

    /**
     * 逻辑删除;0未删除1已删除
     */
    @ApiModelProperty(value = "逻辑删除;0未删除1已删除", required = true)
    @NotNull(message = "逻辑删除;0未删除1已删除不能为空", groups = { EditGroup.class })
    private Integer deleted;


}
