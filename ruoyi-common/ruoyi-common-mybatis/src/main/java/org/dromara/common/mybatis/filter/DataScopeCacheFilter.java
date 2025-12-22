package org.dromara.common.mybatis.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.helper.DataScopeCacheHelper;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * DataScope缓存清理Filter
 * 优先级最高，确保所有请求结束后清理缓存
 */
public class DataScopeCacheFilter implements Filter {

    // 路径匹配器
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    // 硬编码整合：默认排除路径 + 固定白名单
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
        // 默认排除路径
        "/static/**",
        "/error",
        "/*/error",
        "/favicon.ico",
        "/actuator",
        "/actuator/**",
        "/csrf"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 类型安全检查：仅处理HTTP请求
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        // 排除路径判断
        if (StringUtils.isEmpty(requestURI) || isExcludedPath(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            chain.doFilter(request, response);
        } finally {
            DataScopeCacheHelper.clearCache();
        }
    }

    // 判断是否为排除路径
    private boolean isExcludedPath(String requestURI) {
        return EXCLUDED_PATHS.stream()
            .anyMatch(excludedPath -> PATH_MATCHER.match(excludedPath, requestURI));
    }

}
