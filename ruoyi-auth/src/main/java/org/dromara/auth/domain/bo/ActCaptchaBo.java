package org.dromara.auth.domain.bo;

import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import lombok.Data;

@Data
public class ActCaptchaBo {

    private String id;

    private ImageCaptchaTrack data;

}
