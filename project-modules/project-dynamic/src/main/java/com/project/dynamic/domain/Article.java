package com.project.dynamic.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 文章对象 article
 *
 * @author huan.li
 * @date 2022-06-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article")
public class Article extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 发布时间
     */
    private Date pushTime;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 点赞数
     */
    private Long likeCount;
    /**
     * 评论数
     */
    private Long commentCount;
    /**
     * 浏览量
     */
    private Long readCount;
    /**
     * 是否置顶
     */
    private Integer topFlag;
    /**
     * 是否订阅
     */
    private Integer subscribedFlag;
    /**
     * 逻辑删除
     */
    private Integer deleted;

}
