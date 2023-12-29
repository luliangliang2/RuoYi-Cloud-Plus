package org.dromara.resource.convert;

import io.github.linpeilie.BaseMapper;
import org.dromara.common.core.enums.MailType;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.resource.api.domain.bo.RemoteMailBo;
import org.dromara.resource.domain.bo.SysEmailLogBo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.io.InputStream;
import java.util.Map;

/**
 * 邮件对象转换
 *
 * @author 21001
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysEmailLogBoConvert extends BaseMapper<RemoteMailBo, SysEmailLogBo> {

    /**
     * 映射远程邮件对象到邮件日志对象
     *
     * @param remoteMailBo 远程邮件对象
     * @return 邮件日志对象
     */
    @Mapping(target = "imageMap", expression = "java(mapImageMap(remoteMailBo.getImageMap()))")
    SysEmailLogBo mapRemoteMailBo(RemoteMailBo remoteMailBo);

    /**
     * 将图片映射转换为 JSON 字符串
     *
     * @param imageMap 图片映射
     * @return JSON 字符串
     */
    default String mapImageMap(Map<String, InputStream> imageMap) {
        return JsonUtils.toJsonString(imageMap);
    }

    /**
     * 将业务级别枚举转换为字符串
     *
     * @param businessLevel 业务级别枚举
     * @return 业务级别字符串
     */
    default String mapBusinessLevel(MailType.BusinessLevel businessLevel) {
        return businessLevel != null ? businessLevel.getValue() : null;
    }

    /**
     * 将消息类型枚举转换为字符串
     *
     * @param messageType 消息类型枚举
     * @return 消息类型字符串
     */
    default String mapMessageType(MailType.MessageType messageType) {
        return messageType != null ? messageType.getValue() : null;
    }

    /**
     * 将邮件类型枚举转换为字符串
     *
     * @param emailType 邮件类型枚举
     * @return 邮件类型字符串
     */
    default String mapEmailType(MailType.EmailType emailType) {
        return emailType != null ? emailType.getValue() : null;
    }

}
