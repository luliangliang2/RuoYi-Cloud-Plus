package org.dromara.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证参数的基类, 各个认证方式继承此类并扩展参数。
 * 子类需添加 @JsonTypeName(value = "") 注解指定子类型名称，与 grantType 参数一致
 * 反序列化时 Jackson 会根据 grantType 字段内容，匹配注册的子类型 将其反序列化为子类
 * 注册 子类型和名称 详见{@link org.dromara.auth.config.JacksonConfig}
 * 
 * @author lau
 */
@Data
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "grantType",
        visible = true
)
public class AuthParams {
    /**
     * 客户端id
     */
    @NotBlank(message = "{auth.clientid.not.blank}")
    private String clientId;

    /**
     * 客户端key
     */
    private String clientKey;

    /**
     * 客户端秘钥
     */
    private String clientSecret;

    /**
     * 授权类型
     */
    @NotBlank(message = "{auth.grant.type.not.blank}")
    private String grantType;

    /**
     * 租户ID
     */
    private String tenantId;
}
