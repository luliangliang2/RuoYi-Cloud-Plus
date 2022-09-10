package com.ruoyi.db.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据库配置业务对象
 *
 * @author lishuyan
 * @date 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDatabaseConfigBo extends BaseEntity {

    /**
     * 数据库主键
     */
    @NotNull(message = "数据库主键不能为空", groups = {EditGroup.class})
    private Long dbId;

    /**
     * 数据库名称
     */
    @NotBlank(message = "数据库名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 数据库链接地址
     */
    @NotBlank(message = "数据库链接地址不能为空", groups = {AddGroup.class, EditGroup.class})
    private String url;

    /**
     * 数据库用户名
     */
    @NotBlank(message = "数据库用户名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String username;

    /**
     * 数据库密码
     */
    @NotBlank(message = "数据库密码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String password;

    /**
     * 状态（0=正常,1=停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
