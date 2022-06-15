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
 * 工作经历业务对象 user_work_experience
 *
 * @author huan.li
 * @date 2022-06-15
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("工作经历业务对象")
public class UserWorkExperienceBo extends BaseEntity {

    /**
     * 工作经历ID
     */
    @ApiModelProperty(value = "工作经历ID", required = true)
    @NotNull(message = "工作经历ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 代表当前用户;用户信息ID
     */
    @ApiModelProperty(value = "代表当前用户;用户信息ID", required = true)
    @NotNull(message = "代表当前用户;用户信息ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userInfoId;

    /**
     * 公司
     */
    @ApiModelProperty(value = "公司", required = true)
    @NotBlank(message = "公司不能为空", groups = { AddGroup.class, EditGroup.class })
    private String company;

    /**
     * 职位
     */
    @ApiModelProperty(value = "职位", required = true)
    @NotBlank(message = "职位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String position;

    /**
     * 入职时间
     */
    @ApiModelProperty(value = "入职时间", required = true)
    @NotNull(message = "入职时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date entryTime;

    /**
     * 离职时间
     */
    @ApiModelProperty(value = "离职时间", required = true)
    @NotNull(message = "离职时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date departureTime;

    /**
     * 就职时长
     */
    @ApiModelProperty(value = "就职时长", required = true)
    @NotNull(message = "就职时长不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long lengthOfEmployment;

    /**
     * 说明介绍
     */
    @ApiModelProperty(value = "说明介绍", required = true)
    @NotBlank(message = "说明介绍不能为空", groups = { AddGroup.class, EditGroup.class })
    private String introduction;

    /**
     * 搜索值
     */
    @ApiModelProperty(value = "搜索值", required = true)
    @NotBlank(message = "搜索值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String searchValue;

    /**
     * 逻辑删除;0->启用，1->停用
     */
    @ApiModelProperty(value = "逻辑删除;0->启用，1->停用", required = true)
    @NotNull(message = "逻辑删除;0->启用，1->停用不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer deleted;


}
