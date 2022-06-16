package com.project.admin.domain.bo;

import com.project.common.core.validate.AddGroup;
import com.project.common.core.validate.EditGroup;
import com.project.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

import java.util.Date;

/**
 * 学历业务对象 user_education
 *
 * @author huan.li
 * @date 2022-06-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学历业务对象")
public class UserEducationBo extends BaseEntity {

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号", required = true)
    @NotNull(message = "序号不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 用户序号
     */
    @ApiModelProperty(value = "用户序号", required = true)
    @NotNull(message = "用户序号不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long userInfoId;

    /**
     * 学校名称
     */
    @ApiModelProperty(value = "学校名称", required = true)
    @NotBlank(message = "学校名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 入学时间
     */
    @ApiModelProperty(value = "入学时间", required = true)
    @NotNull(message = "入学时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private Date admissionTime;

    /**
     * 毕业时间
     */
    @ApiModelProperty(value = "毕业时间", required = true)
    @NotNull(message = "毕业时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private Date graduationTime;

    /**
     * 专业
     */
    @ApiModelProperty(value = "专业", required = true)
    @NotBlank(message = "专业不能为空", groups = {AddGroup.class, EditGroup.class})
    private String major;

    /**
     * 文化程度
     */
    @ApiModelProperty(value = "文化程度", required = true)
    @NotNull(message = "文化程度不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer geadeLevel;

    /**
     * 说明介绍
     */
    @ApiModelProperty(value = "说明介绍", required = true)
    @NotBlank(message = "说明介绍不能为空", groups = {AddGroup.class, EditGroup.class})
    private String introduction;

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
