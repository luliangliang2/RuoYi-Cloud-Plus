package org.dromara.auth.properties;

import org.dromara.auth.enums.CaptchaCategory;
import org.dromara.auth.enums.InputCaptchaType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置
 *
 * @author ruoyi
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.captcha")
public class CaptchaProperties {

    /**
     * 验证码开关
     */
    private Boolean enabled;

    /**
     * 验证码类型
     */
    private String type;

    /**
     * 输入验证码的相关配置
     */
    private InputCaptchaProperties input;

    /**
     * 行为验证码的相关配置
     */
    private ActCaptchaProperties act;

    @Data
    public static class InputCaptchaProperties {

        /**
         * 验证码类型 math 数组计算, char 字符验证
         */
        private InputCaptchaType type;

        /**
         * 验证码类别 line 线段干扰, circle 圆圈干扰, shear 扭曲干扰, random 随机行为验证码
         */
        private CaptchaCategory category;

        /**
         * 数字验证码位数
         */
        private Integer numberLength;

        /**
         * 字符验证码长度
         */
        private Integer charLength;
    }

    @Data
    public static class ActCaptchaProperties {

        /**
         * 行为验证码类型 random 随机行为验证码
         */
        private String type;
    }

}
