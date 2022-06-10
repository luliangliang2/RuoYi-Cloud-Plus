package com.project.dynamic.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 文章视图对象 article
 *
 * @author huan.li
 * @date 2022-06-10
 */
@Data
@ApiModel("文章视图对象")
@ExcelIgnoreUnannotated
public class ArticleVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 发布时间
     */
    @ExcelProperty(value = "发布时间")
    @ApiModelProperty("发布时间")
    private Date pushTime;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 点赞数
     */
    @ExcelProperty(value = "点赞数")
    @ApiModelProperty("点赞数")
    private Long likeCount;

    /**
     * 评论数
     */
    @ExcelProperty(value = "评论数")
    @ApiModelProperty("评论数")
    private Long commentCount;

    /**
     * 浏览量
     */
    @ExcelProperty(value = "浏览量")
    @ApiModelProperty("浏览量")
    private Long readCount;

    /**
     * 是否置顶
     */
    @ExcelProperty(value = "是否置顶")
    @ApiModelProperty("是否置顶")
    private Integer topFlag;

    /**
     * 是否订阅
     */
    @ExcelProperty(value = "是否订阅")
    @ApiModelProperty("是否订阅")
    private Integer subscribedFlag;


}
