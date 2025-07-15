package org.dromara.common.translation.core.advice;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.translation.core.cache.TranslationCache;
import org.dromara.common.translation.core.handler.TranslationInvocationHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class TranslationResponseAdvice implements ResponseBodyAdvice<Object> {

    private final TranslationCache translationCache;

    @Value("${translation.package}")
    private String translationPackage;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 可以限制 翻译 的路由范围

       return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {
        // 实际请求返回时进行翻译处理
        return new TranslationInvocationHandler(body, translationCache,translationPackage).start();
    }
}
