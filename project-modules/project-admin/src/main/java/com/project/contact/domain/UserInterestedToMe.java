package com.project.contact.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 对我感兴趣对象 user_interested_to_me
 *
 * @author huan.li
 * @date 2022-06-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_interested_to_me")
public class UserInterestedToMe extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * ID
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 当前用户
     */
    private Long userInfoId;
    /**
     * 人脉用户
     */
    private Long contactInfoId;
    /**
     * 搜索值
     */
    private String searchValue;
    /**
     * 逻辑删除;0->未删除，1->删除
     */
    private Integer deleted;

}
