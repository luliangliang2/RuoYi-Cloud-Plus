package com.project.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 学历对象 user_education
 *
 * @author huan.li
 * @date 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_education")
public class UserEducation extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 序号
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 用户序号
     */
    private Long userInfoId;
    /**
     * 学校名称
     */
    private String name;
    /**
     * 入学时间
     */
    private Date admissionTime;
    /**
     * 毕业时间
     */
    private Date graduationTime;
    /**
     * 专业
     */
    private String major;
    /**
     * 文化程度
     */
    private Integer geadeLevel;
    /**
     * 说明介绍
     */
    private String introduction;
    /**
     * 搜索值
     */
    private String searchValue;
    /**
     * 启用状态
     */
    private Integer deleted;

}
