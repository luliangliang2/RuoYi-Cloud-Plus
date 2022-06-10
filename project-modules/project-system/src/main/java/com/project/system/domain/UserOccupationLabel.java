package com.project.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 人脉职业标签对象 user_occupation_label
 *
 * @author project
 * @date 2022-06-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_occupation_label")
public class UserOccupationLabel extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * ID
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 代表当前用户;用户信息ID
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
     * 逻辑删除;0->未删除，1->删除
     */
    private Integer deleted;

}
