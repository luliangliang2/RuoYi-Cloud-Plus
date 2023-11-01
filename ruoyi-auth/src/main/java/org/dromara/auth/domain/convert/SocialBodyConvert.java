package org.dromara.auth.domain.convert;

import io.github.linpeilie.BaseMapper;
import org.dromara.auth.domain.model.SocialAuthParams;
import org.dromara.common.social.domain.model.SocialBody;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 第三方登录body 转换器
 * @author lau
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SocialBodyConvert extends BaseMapper<SocialAuthParams, SocialBody> {
}
