package org.dromara.resource.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 邮件日志对象 sys_email_log
 *
 * @author 2100
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_email_log")
public class SysEmailLog extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @TableId(value = "email_id")
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

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;


}
