package com.project.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 好友分组对象 user_friend_group
 *
 * @author huan.li
 * @date 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_friend_group")
public class UserFriendGroup extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 序号
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 搜索值
     */
    private String searchValue;
    /**
     * 启用状态
     */
    private Integer deleted;

}
