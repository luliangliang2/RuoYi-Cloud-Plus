package org.dromara.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信供应商枚举
 *
 * @author Feng
 */
@Getter
@AllArgsConstructor
public enum SmsType {

    /**
     * 阿里云短信
     */
    ALIBABA("阿里云短信"),

    /**
     * 华为云短信
     */
    HUAWEI("华为云短信"),

    /**
     * 云片
     */
    YUNPIAN("云片短信"),

    /**
     * 腾讯云
     */
    TENCENT("腾讯云短信"),

    /**
     * 合一短信
     */
    UNI_SMS("合一短信"),

    /**
     * 京东云
     */
    JD_CLOUD("京东云短信"),

    /**
     * 容联云
     */
    CLOOPEN("容联云短信"),

    /**
     * 亿美软通
     */
    EMAY("亿美软通"),

    /**
     * 天翼云
     */
    CTYUN("天翼云短信"),

    /**
     * 网易云信
     */
    NETEASE("网易云短信");


    /**
     * 渠道名称
     */
    private final String name;

}
