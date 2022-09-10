package com.ruoyi.db.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 数据库配置视图对象
 *
 * @author lishuyan
 * @date 2022-08-29
 */
@Data
@ExcelIgnoreUnannotated
public class SysDatabaseConfigVo {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库主键
     */
    @ExcelProperty(value = "数据库主键")
    private Long dbId;

    /**
     * 数据库名称
     */
    @ExcelProperty(value = "数据库名称")
    private String name;

    /**
     * 数据库链接地址
     */
    @ExcelProperty(value = "数据库链接地址")
    private String url;

    /**
     * 数据库用户名
     */
    @ExcelProperty(value = "数据库用户名")
    private String username;

    /**
     * 数据库密码
     */
    @ExcelProperty(value = "数据库密码")
    private String password;

    /**
     * 状态（0=正常,1=停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0==正常,1=停用")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ExcelProperty(value = "创建人")
    private String createBy;

}
