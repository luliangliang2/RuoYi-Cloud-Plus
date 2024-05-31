package org.dromara.common.dubbo.generic;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.apache.dubbo.spring.boot.autoconfigure.DubboConfigurationProperties;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.json.utils.JsonUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Dubbo客户端泛化调用
 *
 * @Author ZETA
 * @Date 2024/5/28
 */
public class DubboGenericConsumer {

    private Map<String, ReferenceConfig<GenericService>> referenceConfigMap = new ConcurrentHashMap<>();

    // 获取泛化方法
    public GenericService getGenericService(String interfaceName, String version) {
        String key = interfaceName + ":" + version;
        return referenceConfigMap.computeIfAbsent(key, k -> {
            DubboConfigurationProperties properties = SpringUtils.getBean(DubboConfigurationProperties.class);
            ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
            referenceConfig.setApplication(properties.getApplication());
            referenceConfig.setRegistry(properties.getRegistry());
            referenceConfig.setInterface(interfaceName);
            referenceConfig.setGeneric("true");

            return referenceConfig;
        }).get();
    }

    /**
     * 泛化调用
     *
     * @param interfaceName 接口类全名
     * @param method 方法名
     * @param args 参数
     * @return 返回值
     */
    public Object call(String interfaceName, String method, Object... args){

        String[] collect = Arrays.stream(args)
            .map(t -> t.getClass().getName())
            .toArray(String[]::new);

        GenericService genericService = getGenericService(interfaceName, "1.0.0");
        Object result = genericService.$invoke(method, collect, args);

        return result;
    }
}
