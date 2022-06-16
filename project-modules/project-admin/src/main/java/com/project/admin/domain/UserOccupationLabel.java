package com.project.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 职业标签对象 user_occupation_label
 *
 * @author huan.li
 * @date 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_occupation_label")
public class UserOccupationLabel extends BaseEntity {

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
     * 职业标签
     */
    private String occupationLabel;
    /**
     * 搜索值
     */
    private String searchValue;
    /**
     * 启用状态
     */
    private Integer deleted;

}
