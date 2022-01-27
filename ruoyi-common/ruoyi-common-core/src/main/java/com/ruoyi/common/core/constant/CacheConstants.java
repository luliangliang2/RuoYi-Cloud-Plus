package com.ruoyi.common.core.constant;

/**
 * 缓存的key 常量
 *
 * @author Lion Li
 */
public interface CacheConstants {

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    public static final long CAPTCHA_EXPIRATION = 2;


    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "Authorization:login:token:";

    /**
     * 在线用户 redis key
     */
    public static final String ONLINE_TOKEN_KEY = "online_tokens:";

    /**
     * loginid构造拼接字符串
     */
    public static final String LOGINID_JOIN_CODE = ":";

}
