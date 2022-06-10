package com.project.admin.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户详情视图对象 user_info
 *
 * @author huan.li
 * @date 2022-06-10
 */
@Data
@ApiModel("用户详情视图对象")
@ExcelIgnoreUnannotated
public class UserInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 用户登录id
     */
    @ExcelProperty(value = "用户登录id")
    @ApiModelProperty("用户登录id")
    private Long userLoginId;

    /**
     * 头像
     */
    @ExcelProperty(value = "头像")
    @ApiModelProperty("头像")
    private String headUrl;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称")
    @ApiModelProperty("用户昵称")
    private String nickName;

    /**
     * 真实姓名
     */
    @ExcelProperty(value = "真实姓名")
    @ApiModelProperty("真实姓名")
    private String realName;

    /**
     * 身份证号码
     */
    @ExcelProperty(value = "身份证号码")
    @ApiModelProperty("身份证号码")
    private String idCardNo;

    /**
     * 电话
     */
    @ExcelProperty(value = "电话")
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 学校
     */
    @ExcelProperty(value = "学校")
    @ApiModelProperty("学校")
    private String school;

    /**
     * 学院
     */
    @ExcelProperty(value = "学院")
    @ApiModelProperty("学院")
    private String college;

    /**
     * 年级
     */
    @ExcelProperty(value = "年级")
    @ApiModelProperty("年级")
    private String grade;

    /**
     * 专业
     */
    @ExcelProperty(value = "专业")
    @ApiModelProperty("专业")
    private String major;

    /**
     * 个性签名
     */
    @ExcelProperty(value = "个性签名")
    @ApiModelProperty("个性签名")
    private String personalSignature;

    /**
     * 家乡
     */
    @ExcelProperty(value = "家乡")
    @ApiModelProperty("家乡")
    private String hometown;

    /**
     * 逻辑删除;0未删除1已删除
     */
    @ExcelProperty(value = "逻辑删除;0未删除1已删除")
    @ApiModelProperty("逻辑删除;0未删除1已删除")
    private Integer deleted;


}
