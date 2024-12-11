package org.dromara.auth.controller;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.application.vo.CaptchaResponse;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.common.response.ApiResponse;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.auth.domain.bo.CaptchaBo;
import org.dromara.auth.domain.vo.CaptchaVo;
import org.dromara.auth.enums.CaptchaType;
import org.dromara.auth.enums.InputCaptchaType;
import org.dromara.auth.properties.CaptchaProperties;
import org.dromara.common.core.constant.Constants;
import org.dromara.common.core.constant.GlobalConstants;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.reflect.ReflectUtils;
import org.dromara.common.ratelimiter.annotation.RateLimiter;
import org.dromara.common.ratelimiter.enums.LimitType;
import org.dromara.common.redis.utils.RedisUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 验证码操作处理
 *
 * @author Lion Li
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class CaptchaController {

    private final CaptchaProperties captchaProperties;

    private final ImageCaptchaApplication imageCaptchaApplication;

    /**
     * 生成验证码
     */
    @RateLimiter(time = 60, count = 10, limitType = LimitType.IP)
    @GetMapping("/code")
    public R<CaptchaVo> getCode() {
        CaptchaVo captchaVo = new CaptchaVo();
        boolean captchaEnabled = captchaProperties.getEnabled();
        if (!captchaEnabled) {
            captchaVo.setCaptchaEnabled(false);
            return R.ok(captchaVo);
        }
        captchaVo.setType(captchaProperties.getType());
        if (CaptchaType.ACT.getType().equalsIgnoreCase(captchaProperties.getType())) {
            return R.ok(captchaVo);
        }
        CaptchaProperties.InputCaptchaProperties inputCaptchaProperties = captchaProperties.getInput();
        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        InputCaptchaType inputCaptchaType = inputCaptchaProperties.getType();
        boolean isMath = InputCaptchaType.MATH == inputCaptchaType;
        Integer length = isMath ? inputCaptchaProperties.getNumberLength() : inputCaptchaProperties.getCharLength();
        CodeGenerator codeGenerator = ReflectUtils.newInstance(inputCaptchaType.getClazz(), length);
        AbstractCaptcha captcha = SpringUtils.getBean(inputCaptchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        // 如果是数学验证码，使用SpEL表达式处理验证码结果
        String code = captcha.getCode();
        if (isMath) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
            code = exp.getValue(String.class);
        }
        RedisUtils.setCacheObject(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        captchaVo.setUuid(uuid);
        captchaVo.setImg(captcha.getImageBase64());
        return R.ok(captchaVo);
    }

    /**
     * 生成行为验证码
     */
    @RateLimiter(time = 60, count = 10, limitType = LimitType.IP)
    @PostMapping("/captcha")
    public CaptchaResponse<ImageCaptchaVO> getCaptcha() {
        if (!captchaProperties.getEnabled() || !CaptchaType.ACT.getType().equalsIgnoreCase(captchaProperties.getType())) {
            throw new ServiceException("验证码未开启");
        }
        String type = captchaProperties.getAct().getType();
        if ("RANDOM".equalsIgnoreCase(type)) {
            type = Arrays.asList(CaptchaTypeConstant.SLIDER, CaptchaTypeConstant.CONCAT, CaptchaTypeConstant.ROTATE, CaptchaTypeConstant.WORD_IMAGE_CLICK)
                .get(ThreadLocalRandom.current().nextInt(4));
        }
        return imageCaptchaApplication.generateCaptcha(type);
    }

    /**
     * 校验行为验证码
     */
    @PostMapping("/verify")
    public ApiResponse<?> verify(@RequestBody CaptchaBo data) {
        ApiResponse<?> response = imageCaptchaApplication.matching(data.getId(), data.getData());
        if (response.isSuccess()) {
            return ApiResponse.ofSuccess(Collections.singletonMap("id", data.getId()));
        }
        return response;
    }

}
