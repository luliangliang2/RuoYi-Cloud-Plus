package org.dromara.resource.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.resource.domain.SysEmailLog;

import java.io.Serial;
import java.io.Serializable;


/**
 * 邮件日志视图对象 sys_email_log
 *
 * @author 2100
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SysEmailLog.class)
public class SysEmailLogVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @ExcelProperty(value = "日志主键")
    private Long emailId;

    /**
     * 业务级别（0系统 1租户 2部门）
     */
    @ExcelProperty(value = "业务级别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "email_business_level")
    private String businessLevel;

    /**
     * 消息类型（0单个 1多个）
     */
    @ExcelProperty(value = "消息类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "email_message_type")
    private String messageType;

    /**
     * message-id
     */
    @ExcelProperty(value = "message-id")
    private String messageId;

    /**
     * 收件人
     */
    @ExcelProperty(value = "收件人")
    private String tos;

    /**
     * 抄送人
     */
    @ExcelProperty(value = "抄送人")
    private String ccs;

    /**
     * 密送人
     */
    @ExcelProperty(value = "密送人")
    private String bccs;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String subject;

    /**
     * 邮件类型（0文本 1HTML）
     */
    @ExcelProperty(value = "邮件类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_email_type")
    private String emailType;

    /**
     * 发送状态（0成功 1失败）
     */
    @ExcelProperty(value = "发送状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_email_status")
    private String status;


}
