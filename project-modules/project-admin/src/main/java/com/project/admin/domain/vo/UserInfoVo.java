package com.project.admin.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.project.common.excel.annotation.ExcelDictFormat;
import com.project.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户信息视图对象 user_info
 *
 * @author huan.li
 * @date 2022-06-16
 */
@Data
@ApiModel("用户信息视图对象")
@ExcelIgnoreUnannotated
public class UserInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @ExcelProperty(value = "序号")
    @ApiModelProperty("序号")
    private Long id;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名")
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    @ApiModelProperty("昵称")
    private String nick;

    /**
     * 手机号码
     */
    @ExcelProperty(value = "手机号码")
    @ApiModelProperty("手机号码")
    private String phone;

    /**
     * 影响力
     */
    @ExcelProperty(value = "影响力")
    @ApiModelProperty("影响力")
    private Long effectCount;

    /**
     * 访客数量
     */
    @ExcelProperty(value = "访客数量")
    @ApiModelProperty("访客数量")
    private Long visitorCount;

    /**
     * 公司
     */
    @ExcelProperty(value = "公司")
    @ApiModelProperty("公司")
    private String company;

    /**
     * 职位
     */
    @ExcelProperty(value = "职位")
    @ApiModelProperty("职位")
    private String position;

    /**
     * 自我介绍
     */
    @ExcelProperty(value = "自我介绍")
    @ApiModelProperty("自我介绍")
    private String selfIntroduction;

    /**
     * 职业方向
     */
    @ExcelProperty(value = "职业方向")
    @ApiModelProperty("职业方向")
    private String careerDirection;

    /**
     * 所在位置
     */
    @ExcelProperty(value = "所在位置")
    @ApiModelProperty("所在位置")
    private String location;

    /**
     * 家乡
     */
    @ExcelProperty(value = "家乡")
    @ApiModelProperty("家乡")
    private String hometown;

    /**
     * 星座
     */
    @ExcelProperty(value = "星座", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "constellation")
    @ApiModelProperty("星座")
    private Integer constellation;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱")
    @ApiModelProperty("邮箱")
    private String email;

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
