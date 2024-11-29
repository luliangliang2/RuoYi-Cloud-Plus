package org.dromara.auth.domain.vo;

import lombok.Data;

/**
 * 验证码配置
 *
 * @author 疯狂的牛子Li
 */
@Data
public class CaptchaConfigVo {

    /**
     * 是否开启验证码
     */
    private Boolean captchaEnabled = true;

    /**
     * 验证码类型
     */
    private String type;

}
