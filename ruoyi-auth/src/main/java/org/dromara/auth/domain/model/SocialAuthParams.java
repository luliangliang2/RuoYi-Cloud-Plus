package org.dromara.auth.domain.model;


import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lau
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@JsonTypeName(value = "social")
public class SocialAuthParams extends AuthParams{
    /**
     * 第三方登录平台
     */
    @NotBlank(message = "{social.source.not.blank}" )
    private String source;

    /**
     * 第三方登录code
     */
    @NotBlank(message = "{social.code.not.blank}" )
    private String socialCode;

    /**
     * 第三方登录socialState
     */
    @NotBlank(message = "{social.state.not.blank}" )
    private String socialState;

}
