package com.project.dynamic.domain.bo;

import com.project.common.core.validate.AddGroup;
import com.project.common.core.validate.EditGroup;
import com.project.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

/**
 * 文章业务对象 article
 *
 * @author huan.li
 * @date 2022-06-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("文章业务对象")
public class ArticleBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间", required = true)
    @NotNull(message = "发布时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date pushTime;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 点赞数
     */
    @ApiModelProperty(value = "点赞数", required = true)
    @NotNull(message = "点赞数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long likeCount;

    /**
     * 评论数
     */
    @ApiModelProperty(value = "评论数", required = true)
    @NotNull(message = "评论数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long commentCount;

    /**
     * 浏览量
     */
    @ApiModelProperty(value = "浏览量", required = true)
    @NotNull(message = "浏览量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long readCount;

    /**
     * 是否置顶
     */
    @ApiModelProperty(value = "是否置顶", required = true)
    @NotNull(message = "是否置顶不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer topFlag;

    /**
     * 是否订阅
     */
    @ApiModelProperty(value = "是否订阅", required = true)
    @NotNull(message = "是否订阅不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer subscribedFlag;


}
