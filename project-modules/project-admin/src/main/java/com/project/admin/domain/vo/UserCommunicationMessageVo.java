package com.project.admin.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 沟通消息视图对象 user_communication_message
 *
 * @author huan.li
 * @date 2022-06-16
 */
@Data
@ApiModel("沟通消息视图对象")
@ExcelIgnoreUnannotated
public class UserCommunicationMessageVo {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @ExcelProperty(value = "序号")
    @ApiModelProperty("序号")
    private Long id;

    /**
     * 用户序号
     */
    @ExcelProperty(value = "用户序号")
    @ApiModelProperty("用户序号")
    private Long userInfoId;

    /**
     * 消息内容
     */
    @ExcelProperty(value = "消息内容")
    @ApiModelProperty("消息内容")
    private String messageContent;

    /**
     * 消息状态
     */
    @ExcelProperty(value = "消息状态")
    @ApiModelProperty("消息状态")
    private Integer messageStatus;

    /**
     * 搜索值
     */
    @ExcelProperty(value = "搜索值")
    @ApiModelProperty("搜索值")
    private String searchValue;

    /**
     * 启用状态
     */
    @ExcelProperty(value = "启用状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "deleted")
    @ApiModelProperty("启用状态")
    private Integer deleted;


}
