package org.dromara.magicapi.config;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * 将若依标准 Authorization 请求头适配给 Magic API 2.2.2 内部固定读取的 Magic-Token。
 *
 * @author ruoyi
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class MagicApiAuthorizationHeaderFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String MAGIC_TOKEN_HEADER = "Magic-Token";

    private static final String MAGIC_UNAUTHORIZED_TOKEN = "unauthorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        filterChain.doFilter(new MagicApiAuthorizationHeaderRequestWrapper(request), response);
    }

    private static class MagicApiAuthorizationHeaderRequestWrapper extends HttpServletRequestWrapper {

        MagicApiAuthorizationHeaderRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            if (StrUtil.equalsIgnoreCase(MAGIC_TOKEN_HEADER, name)) {
                String authorization = super.getHeader(AUTHORIZATION_HEADER);
                if (StrUtil.isNotBlank(authorization)) {
                    return authorization;
                }
                String magicToken = super.getHeader(name);
                return MAGIC_UNAUTHORIZED_TOKEN.equalsIgnoreCase(magicToken) ? null : magicToken;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            String header = getHeader(name);
            if (header == null) {
                return Collections.emptyEnumeration();
            }
            return Collections.enumeration(Collections.singleton(header));
        }
    }
}
