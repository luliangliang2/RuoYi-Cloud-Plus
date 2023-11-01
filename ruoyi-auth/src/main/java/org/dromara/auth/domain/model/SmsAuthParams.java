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
@JsonTypeName(value = "sms")
public class SmsAuthParams extends AuthParams {
    /**
     * 手机号
     */
    @NotBlank(message = "{user.phonenumber.not.blank}")
    private String phonenumber;

    /**
     * 短信code
     */
    @NotBlank(message = "{sms.code.not.blank}")
    private String smsCode;
}
