package org.ssssssss.magicapi.net;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

/**
 * Net 插件自动配置
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "magic-api", name = "web")
@Import({MagicNetConfiguration.class})
public class MagicAPINetAutoConfiguration {
}
