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
@JsonTypeName(value = "xcx")
public class XcxAuthParams extends AuthParams {
    /**
     * 小程序code
     */
    @NotBlank(message = "{xcx.code.not.blank}")
    private String xcxCode;
}
