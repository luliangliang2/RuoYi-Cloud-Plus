package org.dromara.common.social.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录对象
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialBody {


    /**
     * 第三方登录平台
     */
    private String source;

    /**
     * 第三方登录code
     */
    private String socialCode;

    /**
     * 第三方登录socialState
     */
    private String socialState;

}
