package org.dromara.magicapi.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * 兼容 Magic API 2.2.2 在 Spring Boot 3.4 下重复注册 Jakarta 拦截器的问题。
 *
 * @author ruoyi
 */
@Component
public class MagicApiBeanDefinitionPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    private static final String DUPLICATE_MAGIC_INTERCEPTOR_BEAN_NAME = "MagicJakartaWebRequestInterceptor";

    private static final String MAGIC_INTERCEPTOR_BEAN_NAME = "magicWebRequestInterceptor";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (!registry.containsBeanDefinition(DUPLICATE_MAGIC_INTERCEPTOR_BEAN_NAME)
            || !registry.containsBeanDefinition(MAGIC_INTERCEPTOR_BEAN_NAME)) {
            return;
        }
        BeanDefinition duplicateDefinition = registry.getBeanDefinition(DUPLICATE_MAGIC_INTERCEPTOR_BEAN_NAME);
        BeanDefinition magicDefinition = registry.getBeanDefinition(MAGIC_INTERCEPTOR_BEAN_NAME);
        if (isMagicJakartaServletConfiguration(duplicateDefinition) && isMagicJakartaServletConfiguration(magicDefinition)) {
            registry.removeBeanDefinition(DUPLICATE_MAGIC_INTERCEPTOR_BEAN_NAME);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // no-op
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private boolean isMagicJakartaServletConfiguration(BeanDefinition beanDefinition) {
        return "org.ssssssss.magicapi.servlet.jakarta.MagicJakartaServletConfiguration"
            .equals(beanDefinition.getFactoryBeanName());
    }
}
