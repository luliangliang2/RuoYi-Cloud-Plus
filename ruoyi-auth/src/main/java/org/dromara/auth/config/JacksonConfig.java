package org.dromara.auth.config;

import cn.hutool.core.util.ClassUtil;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.dromara.auth.domain.model.AuthParams;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.Set;

/**
 * 替换默认的 ObjectMapper 注册 AuthParams 子类型
 * @author lau
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Jackson2ObjectMapperBuilder.class})
public class JacksonConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 注册 AuthParams 子类型
        registerAuthParamsSubType(objectMapper);

        return objectMapper;
    }

    /**
     * 将指定AuthParams的子类注册为子类型，以便反序列化时将其序列化为具体的子类
     */
    private void registerAuthParamsSubType(ObjectMapper objectMapper){
        // 获取 AuthParams 所在包内的所有子类
        Set<Class<?>> subClass = ClassUtil.scanPackageBySuper(ClassUtil.getPackage(AuthParams.class), AuthParams.class);
        // 获取名称并注册
        for (Class<?> aClass : subClass) {
            JsonTypeName jsonTypeName = aClass.getAnnotation(JsonTypeName.class);
            String name = jsonTypeName == null ? aClass.getSimpleName() : jsonTypeName.value();
            objectMapper.registerSubtypes(new NamedType(aClass, name));
        }
    }


}