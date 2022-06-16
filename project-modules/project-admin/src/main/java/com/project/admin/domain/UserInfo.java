package com.project.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户信息对象 user_info
 *
 * @author huan.li
 * @date 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_info")
public class UserInfo extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 序号
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 昵称
     */
    private String nick;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 影响力
     */
    private Long effectCount;
    /**
     * 访客数量
     */
    private Long visitorCount;
    /**
     * 公司
     */
    private String company;
    /**
     * 职位
     */
    private String position;
    /**
     * 自我介绍
     */
    private String selfIntroduction;
    /**
     * 职业方向
     */
    private String careerDirection;
    /**
     * 所在位置
     */
    private String location;
    /**
     * 家乡
     */
    private String hometown;
    /**
     * 星座
     */
    private Integer constellation;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 搜索值
     */
    private String searchValue;
    /**
     * 启用状态
     */
    private Integer deleted;

}
