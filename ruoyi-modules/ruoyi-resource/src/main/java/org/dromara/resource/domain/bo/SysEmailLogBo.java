package org.dromara.resource.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.resource.domain.SysEmailLog;

/**
 * 邮件日志业务对象 sys_email_log
 *
 * @author 2100
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysEmailLog.class, reverseConvertGenerate = false)
public class SysEmailLogBo extends BaseEntity {

    /**
     * 日志主键
     */
    private Long emailId;

    /**
     * 业务级别（0系统 1租户 2部门）
     */
    private String businessLevel;

    /**
     * 消息类型（0单个 1多个）
     */
    private String messageType;

    /**
     * message-id
     */
    private String messageId;

    /**
     * 收件人
     */
    private String tos;

    /**
     * 抄送人
     */
    private String ccs;

    /**
     * 密送人
     */
    private String bccs;

    /**
     * 标题
     */
    private String subject;

    /**
     * 正文
     */
    private String content;

    /**
     * 邮件类型（0文本 1HTML）
     */
    private String emailType;

    /**
     * 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     */
    private String imageMap;

    /**
     * 附件列表
     */
    private String files;

    /**
     * 发送状态（0成功 1失败）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
