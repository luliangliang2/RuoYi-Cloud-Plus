package org.dromara.common.dubbo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * DubboCommonProperties
 *
 * @Author ZETA
 * @Date 2024/5/29
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "dubbo.custom")
public class DubboCommonProperties {
}
