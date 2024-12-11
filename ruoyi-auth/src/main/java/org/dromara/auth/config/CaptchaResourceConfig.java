package org.dromara.auth.config;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class CaptchaResourceConfig {

    private final ResourceStore resourceStore;

    @PostConstruct
    public void init() {
        // 添加自定义背景图片
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/1.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/2.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/3.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/4.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/5.jpg", "default"));

        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "bgimages/1.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "bgimages/2.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "bgimages/3.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "bgimages/4.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "bgimages/5.jpg", "default"));

        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "bgimages/1.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "bgimages/2.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "bgimages/3.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "bgimages/4.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "bgimages/5.jpg", "default"));

        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "bgimages/1.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "bgimages/2.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "bgimages/3.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "bgimages/4.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "bgimages/5.jpg", "default"));
    }
}
