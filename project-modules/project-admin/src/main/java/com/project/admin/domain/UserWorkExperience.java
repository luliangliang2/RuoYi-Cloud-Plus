package com.project.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 工作经历对象 user_work_experience
 *
 * @author huan.li
 * @date 2022-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_work_experience")
public class UserWorkExperience extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 工作经历ID
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 代表当前用户;用户信息ID
     */
    private Long userInfoId;
    /**
     * 公司
     */
    private String company;
    /**
     * 职位
     */
    private String position;
    /**
     * 入职时间
     */
    private Date entryTime;
    /**
     * 离职时间
     */
    private Date departureTime;
    /**
     * 就职时长
     */
    private Long lengthOfEmployment;
    /**
     * 说明介绍
     */
    private String introduction;
    /**
     * 搜索值
     */
    private String searchValue;
    /**
     * 逻辑删除;0->启用，1->停用
     */
    private Integer deleted;

}
