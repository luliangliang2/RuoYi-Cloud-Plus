package com.project.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 好友关系对象 user_friend_relation
 *
 * @author huan.li
 * @date 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_friend_relation")
public class UserFriendRelation extends BaseEntity {

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
     * 好友序号
     */
    private Long friendInfoId;
    /**
     * 分组序号
     */
    private Long userFriendGroupId;
    /**
     * 关系状态序号
     */
    private Long userFriendRelationStatusId;
    /**
     * 交换状态
     */
    private Integer exchangePhoneStatus;
    /**
     * 搜索值
     */
    private String searchValue;
    /**
     * 启用状态
     */
    private Integer deleted;

}
