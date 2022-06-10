package com.project.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户详情对象 user_info
 *
 * @author huan.li
 * @date 2022-06-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_info")
public class UserInfo extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 用户登录id
     */
    private Long userLoginId;
    /**
     * 头像
     */
    private String headUrl;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 身份证号码
     */
    private String idCardNo;
    /**
     * 电话
     */
    private String phone;
    /**
     * 学校
     */
    private String school;
    /**
     * 学院
     */
    private String college;
    /**
     * 年级
     */
    private String grade;
    /**
     * 专业
     */
    private String major;
    /**
     * 个性签名
     */
    private String personalSignature;
    /**
     * 家乡
     */
    private String hometown;
    /**
     * 逻辑删除;0未删除1已删除
     */
    private Integer deleted;

}
