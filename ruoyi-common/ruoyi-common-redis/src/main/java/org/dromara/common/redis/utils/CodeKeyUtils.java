package org.dromara.common.redis.utils;

import cn.hutool.core.util.RandomUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.common.core.constant.Constants;
import org.dromara.common.core.constant.GlobalConstants;
import org.dromara.common.core.exception.user.CaptchaExpireException;
import org.dromara.common.core.utils.StringUtils;

import java.time.Duration;

/**
 * 验证码 工具类
 *
 * @author 21001
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeKeyUtils {

    /**
     * 生成并缓存验证码的实用方法
     *
     * @param key 与生成的验证码相关联的键
     * @return 生成的验证码
     */
    public static String captchaCodeKey(String key) {
        // 生成一个包含4位数字的随机验证码
        String captcha = RandomUtil.randomNumbers(4);
        // 将验证码存储到缓存中，并设置过期时间
        RedisUtils.setCacheObject(getVerifyKey(key), captcha, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        // 返回生成的验证码
        return captcha;
    }

    /**
     * 缓存指定验证码的实用方法
     *
     * @param key     与缓存中验证码相关联的键
     * @param captcha 要缓存的验证码
     */
    public static void captchaCodeKey(String key, String captcha) {
        // 将验证码存储到缓存中，并设置过期时间
        RedisUtils.setCacheObject(getVerifyKey(key), captcha, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
    }

    /**
     * 验证验证码的静态方法。
     *
     * @param key  与缓存中验证码相关联的键。
     * @param code 要验证的验证码。
     * @return 如果验证码验证成功，则返回 true；否则返回 false。
     * @throws CaptchaExpireException 如果验证码过期。
     */
    public static boolean validateCode(String key, String code) {
        // 从缓存中检索存储的验证码
        String captcha = RedisUtils.getCacheObject(getVerifyKey(key));

        // 检查存储的验证码是否为空，表示已过期
        if (StringUtils.isBlank(captcha)) {
            throw new CaptchaExpireException();
        }

        // 返回验证码是否与输入的验证码匹配
        return captcha.equals(code);
    }

    /**
     * 从缓存中删除指定键的验证码
     *
     * @param key 与要删除的验证码相关联的键
     */
    public static void deleteVerifyKey(String key) {
        // 从缓存中删除指定键的验证码
        RedisUtils.deleteObject(getVerifyKey(key));
    }

    /**
     * 获取验证码的键（verify key）
     *
     * @param key 与验证码相关联的键
     * @return 完整的验证码的键
     */
    private static String getVerifyKey(String key) {
        return GlobalConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(key, "");
    }
}
