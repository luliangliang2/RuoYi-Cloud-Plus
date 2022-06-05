package com.project.admin.contact.domain.vo;

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
 * @author project
 * @date 2022-06-05
 */
@Data
@ApiModel("沟通消息视图对象")
@ExcelIgnoreUnannotated
public class UserCommunicationMessageVo {

    private static final long serialVersionUID = 1L;

    /**
     * 逻辑删除;0未删除1已删除
     */
    @ExcelProperty(value = "逻辑删除;0未删除1已删除")
    @ApiModelProperty("逻辑删除;0未删除1已删除")
    private Integer deleted;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 代表当前用户
     */
    @ExcelProperty(value = "代表当前用户")
    @ApiModelProperty("代表当前用户")
    private Long userInfoId;

    /**
     * 代表人脉用户
     */
    @ExcelProperty(value = "代表人脉用户")
    @ApiModelProperty("代表人脉用户")
    private Long contactInfoId;

    /**
     * 消息内容
     */
    @ExcelProperty(value = "消息内容")
    @ApiModelProperty("消息内容")
    private String messageContent;


}
