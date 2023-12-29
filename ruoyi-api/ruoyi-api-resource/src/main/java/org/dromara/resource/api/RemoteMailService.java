package org.dromara.resource.api;

import org.dromara.resource.api.domain.RemoteMailSettings;
import org.dromara.resource.api.domain.bo.RemoteMailBo;

/**
 * 邮件服务（建议用异步）
 *
 * @author Feng
 */
public interface RemoteMailService {

    /**
     * 发送邮件方法，使用默认的邮件帐户信息和指定的邮件信息对象
     *
     * @param bo 包含邮件信息的对象，包括收件人、抄送、主题、正文内容等
     * @return 返回发送邮件操作的结果，通常是邮件的唯一标识符（messageId）
     */
    String sendMail(RemoteMailBo bo);

    /**
     * 发送邮件方法，使用指定的邮件帐户信息和包含邮件信息的对象
     *
     * @param settings 邮件帐户信息，包括邮件服务器主机、端口、用户名、密码等
     * @param bo       包含邮件信息的对象，包括收件人、抄送、主题、正文内容等
     * @return 返回发送邮件操作的结果，通常是邮件的唯一标识符（messageId）
     */
    String sendMail(RemoteMailSettings settings, RemoteMailBo bo);

}
