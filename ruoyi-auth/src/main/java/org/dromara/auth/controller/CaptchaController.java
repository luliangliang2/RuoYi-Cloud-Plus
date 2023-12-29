package org.dromara.auth.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.auth.domain.vo.CaptchaVo;
import org.dromara.auth.enums.CaptchaType;
import org.dromara.auth.properties.CaptchaProperties;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.reflect.ReflectUtils;
import org.dromara.common.ratelimiter.annotation.RateLimiter;
import org.dromara.common.ratelimiter.enums.LimitType;
import org.dromara.common.redis.utils.CodeKeyUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码操作处理
 *
 * @author Lion Li
 */
@SaIgnore
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class CaptchaController {

    private final CaptchaProperties captchaProperties;

    /**
     * 生成验证码
     */
    @RateLimiter(time = 60, count = 10, limitType = LimitType.IP)
    @GetMapping("/code")
    public R<CaptchaVo> getCode() {
        // 创建一个用于返回验证码信息的实体类
        CaptchaVo captchaVo = new CaptchaVo();

        // 检查验证码是否启用
        boolean captchaEnabled = captchaProperties.getEnabled();
        if (!captchaEnabled) {
            // 如果验证码未启用，设置相应的标志并返回响应
            captchaVo.setCaptchaEnabled(false);
            return R.ok(captchaVo);
        }

        // 生成一个唯一的UUID作为验证码的标识
        String uuid = IdUtil.simpleUUID();

        // 根据配置生成验证码
        CaptchaType captchaType = captchaProperties.getType();
        //获取验证码类型，是否是数组计算MATH
        boolean isMath = CaptchaType.MATH == captchaType;
        //如果是则拿取数字验证码位数，如果不是则拿取字符验证码长度
        Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();
        CodeGenerator codeGenerator = ReflectUtils.newInstance(captchaType.getClazz(), length);
        AbstractCaptcha captcha = SpringUtils.getBean(captchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        String code = captcha.getCode();

        // 如果是数学验证码，使用SpEL表达式处理验证码结果
        if (isMath) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
            code = exp.getValue(String.class);
        }

        // 缓存生成的验证码
        CodeKeyUtils.captchaCodeKey(uuid, code);

        // 设置返回实体的相关属性
        captchaVo.setUuid(uuid);
        captchaVo.setImg(captcha.getImageBase64());

        // 返回包含验证码信息的响应
        return R.ok(captchaVo);
    }

}
