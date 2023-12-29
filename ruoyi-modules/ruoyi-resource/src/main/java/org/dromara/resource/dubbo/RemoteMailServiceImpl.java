package org.dromara.resource.dubbo;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.core.enums.MailType;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.ValidatorUtils;
import org.dromara.common.mail.config.properties.MailProperties;
import org.dromara.common.mail.utils.MailAccount;
import org.dromara.common.mail.utils.MailUtils;
import org.dromara.resource.api.RemoteMailService;
import org.dromara.resource.api.domain.RemoteMailSettings;
import org.dromara.resource.api.domain.bo.RemoteMailBo;
import org.dromara.resource.domain.bo.SysEmailLogBo;
import org.dromara.resource.domain.vo.SysOssVo;
import org.dromara.resource.service.ISysEmailLogService;
import org.dromara.resource.service.ISysOssService;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 邮件服务
 *
 * @author Feng
 */
@Slf4j
@RequiredArgsConstructor
@Service
@DubboService(timeout = 10000)
public class RemoteMailServiceImpl implements RemoteMailService {
    private final MailProperties mailProperties;
    private final ISysOssService sysOssService;
    private final ISysEmailLogService sysEmailLogService;

    /**
     * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人
     *
     * @param bo 包含邮件信息的对象
     * @return messageId
     * @throws ServiceException 如果邮件发送失败
     */
    @Override
    public String sendMail(RemoteMailBo bo) {
        return settingsMail(null, bo);
    }

    /**
     * 发送邮件方法，使用指定的邮件帐户信息和包含邮件信息的对象
     *
     * @param settings 邮件帐户信息，包括邮件服务器主机、端口、用户名、密码等
     * @param bo       包含邮件信息的对象，包括收件人、抄送、主题、正文内容等
     * @return 返回发送邮件操作的结果，通常是邮件的唯一标识符（messageId）
     */
    @Override
    public String sendMail(RemoteMailSettings settings, RemoteMailBo bo) {
        // 对邮件帐户信息进行验证
        ValidatorUtils.validate(settings);
        return settingsMail(settings, bo);
    }

    /**
     * 发送邮件并记录邮件日志
     *
     * @param settings 邮件帐户信息，包括邮件服务器主机、端口、用户名、密码等
     * @param bo       包含邮件信息的对象，包括收件人、抄送、主题、正文内容等
     * @return 返回发送邮件的消息ID。如果发送失败，则抛出ServiceException异常，并记录邮件发送失败的日志
     */
    private String settingsMail(RemoteMailSettings settings, RemoteMailBo bo) {
        if (!mailProperties.getEnabled()) {
            throw new ServiceException("当前系统没有开启邮箱功能");
        }

        // 获取邮件帐户对象
        MailAccount mailAccount = getMailAccount(settings);

        // 验证邮件信息对象
        ValidatorUtils.validate(bo);

        // 将 RemoteMailBo 转换为 SysEmailLogBo，用于记录邮件日志
        SysEmailLogBo logBo = MapstructUtils.convert(bo, SysEmailLogBo.class);

        try {
            File[] attachments = bo.getAttachments();
            // 获取附件文件ID字符串
            String files = uploads(attachments);

            // 判断是否是 HTML 邮件
            boolean isHtml = MailType.EmailType.HTML == bo.getEmailType();

            // 发送邮件并获取message-id
            String messageId = MailUtils.send(mailAccount, bo.getTos(), bo.getCcs(), bo.getBccs(), bo.getSubject(), bo.getContent(), bo.getImageMap(), isHtml, attachments);

            // 设置消息ID和成功状态到邮件日志对象
            logBo.setMessageId(messageId);
            logBo.setStatus(MailType.EmailStatus.SUCCESS.getValue());
            logBo.setFiles(files);
            return messageId;
        } catch (Exception e) {
            // 如果发送邮件失败，设置失败状态到邮件日志对象，并记录错误信息
            log.error("邮件发送失败：{}，{}" + e.getMessage(), e);
            if (StringUtils.isEmpty(logBo.getRemark())) {
                logBo.setContent(e.getMessage());
            }
            logBo.setRemark(MailType.EmailStatus.FAILURE.getValue());
            throw new ServiceException("邮件发送失败：" + e.getMessage());
        } finally {
            // 无论发送是否成功，都记录邮件日志到数据库
            sysEmailLogService.insertByBo(logBo);
        }
    }

    /**
     * 根据给定的邮件帐户设置生成邮件帐户对象。
     *
     * @param settings 邮件帐户信息，包括邮件服务器主机、端口、用户名、密码等。
     * @return 返回生成的邮件帐户对象，如果输入的邮件帐户信息为null，则返回null。
     */
    private MailAccount getMailAccount(RemoteMailSettings settings) {
        if (ObjectUtil.isNull(settings)) {
            return null;
        }

        // 创建邮件帐户对象并设置相应属性
        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(settings.getHost());
        mailAccount.setPort(settings.getPort());
        mailAccount.setAuth(settings.getAuth());
        mailAccount.setUser(settings.getUser());
        mailAccount.setPass(settings.getPass());
        mailAccount.setFrom(settings.getFrom());

        // 返回生成的邮件帐户对象
        return mailAccount;
    }


    /**
     * 批量上传文件并获取上传ID字符串，使用逗号拼接。
     *
     * @param files 要上传的文件数组。
     * @return 返回上传文件的ID字符串，多个ID使用逗号分隔。如果输入的文件数组为null，则返回null。
     */
    private String uploads(File[] files) {
        if (ObjectUtil.isEmpty(files)) {
            return null;
        }

        StringBuilder fileListBuilder = new StringBuilder();

        for (File file : files) {
            SysOssVo upload = sysOssService.upload(file);

            // 将上传的文件ID追加到文件列表字符串中，使用逗号分隔
            if (fileListBuilder.length() > 0) {
                fileListBuilder.append(",");
            }
            fileListBuilder.append(upload.getOssId());
        }

        // 返回上传文件的ID字符串
        return fileListBuilder.toString();
    }

}
