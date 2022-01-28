package com.ruoyi.common.security.config;

import cn.dev33.satoken.jwt.StpLogicJwtForStyle;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token 整合 jwt (Style模式)
 *
 * @author Lion Li
 */
@Configuration
public class SaTokenConfiguration {

    /**
     * 注册sa-token的拦截器
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册路由拦截器，自定义验证规则
//        registry.addInterceptor(new SaRouteInterceptor() {
//            @SuppressWarnings("all")
//            @Override
//            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//                LoginHelper.clearCache();
//            }
//        }).addPathPatterns("/**");
//        registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");
//    }

    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForStyle();
    }

}
