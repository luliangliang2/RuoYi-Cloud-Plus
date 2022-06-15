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
 * @date 2022-06-15
 */
@Data
@ApiModel("沟通消息视图对象")
@ExcelIgnoreUnannotated
public class UserCommunicationMessageVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 发送消息用户
     */
    @ExcelProperty(value = "发送消息用户")
    @ApiModelProperty("发送消息用户")
    private Long sendMessageId;

    /**
     * 消息内容
     */
    @ExcelProperty(value = "消息内容")
    @ApiModelProperty("消息内容")
    private String messageContent;

    /**
     * 是否由我发送
     */
    @ExcelProperty(value = "是否由我发送", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "yes_or_no")
    @ApiModelProperty("是否由我发送")
    private Integer sendFromMeStatus;

    /**
     * 逻辑删除
     */
    @ExcelProperty(value = "逻辑删除", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "deleted")
    @ApiModelProperty("逻辑删除")
    private Integer deleted;


}
