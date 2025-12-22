package org.dromara.common.mybatis.config;

import org.dromara.common.mybatis.filter.DataScopeCacheFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * DataScope配置类
 */
@AutoConfiguration
public class DataScopeConfig {

    /**
     * 创建Filter Bean
     */
    @Bean
    @ConditionalOnMissingBean(name = "dataScopeCacheFilter")
    public DataScopeCacheFilter dataScopeCacheFilter() {
        return new DataScopeCacheFilter();
    }

    /**
     * 注册Filter到Servlet容器（指定拦截路径+优先级）
     */
    @Bean
    @ConditionalOnMissingBean(name = "dataScopeCacheFilterRegistration")
    public FilterRegistrationBean<DataScopeCacheFilter> dataScopeCacheFilterRegistration() {
        FilterRegistrationBean<DataScopeCacheFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(dataScopeCacheFilter());
        // 拦截所有请求
        registrationBean.addUrlPatterns("/*");
        // 最高优先级（确保最后清理）
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }
}
