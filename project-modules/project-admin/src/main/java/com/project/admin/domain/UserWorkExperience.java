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
 * @date 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_work_experience")
public class UserWorkExperience extends BaseEntity {

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
     * 启用状态
     */
    private Integer deleted;

}
