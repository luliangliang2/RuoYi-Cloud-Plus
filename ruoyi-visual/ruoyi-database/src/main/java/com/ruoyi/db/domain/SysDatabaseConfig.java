package com.ruoyi.db.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 数据库配置对象 sys_database_config
 *
 * @author lishuyan
 * @date 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_database_config")
public class SysDatabaseConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库主键
     */
    @TableId(value = "db_id")
    private Long dbId;
    /**
     * 数据库名称
     */
    private String name;
    /**
     * 数据库链接地址
     */
    private String url;
    /**
     * 数据库用户名
     */
    private String username;
    /**
     * 数据库密码
     */
    private String password;
    /**
     * 状态（0=正常,1=停用）
     */
    private String status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createBy;


}
