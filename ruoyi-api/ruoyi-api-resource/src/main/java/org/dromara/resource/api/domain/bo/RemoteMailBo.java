package org.dromara.resource.api.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.common.core.enums.MailType;

import java.io.File;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 邮件业务对象
 *
 * @author Feng
 */
@Data
@NoArgsConstructor
public class RemoteMailBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 业务级别（0系统 1租户 2部门）
     */
    @NotNull(message = "业务级别不能为空")
    private MailType.BusinessLevel businessLevel;

    /**
     * 消息类型（0单个 1多个）
     */
    @NotNull(message = "消息类型不能为空")
    private MailType.MessageType messageType;

    /**
     * 收件人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     */
    @NotBlank(message = "收件人不能为空")
    private String tos;

    /**
     * 抄送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     */
    private String ccs;

    /**
     * 密送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     */
    private String bccs;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    private String subject;

    /**
     * 正文
     */
    @NotBlank(message = "标题不能为空")
    private String content;

    /**
     * 邮件类型（0文本 1HTML）
     */
    @NotNull(message = "邮件类型不能为空")
    private MailType.EmailType emailType;

    /**
     * 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     */
    private Map<String, InputStream> imageMap;

    /**
     * 附件列表
     */
    private File[] attachments;

    /**
     * 备注
     */
    private String remark;

}
