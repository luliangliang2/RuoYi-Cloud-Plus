package org.dromara.common.translation.core.cache;


import cn.hutool.core.collection.CollectionUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.translation.core.TranslationInterface;
import org.springframework.stereotype.Component;

/**
 * @author zc
 */
@Component
@Slf4j
public class TranslationCache {

    public static final Map<String, TranslationInterface<?>> TRANSLATION_MAPPER = new ConcurrentHashMap<>();


    /**
     *
     * @param type 翻译类型
     * @param ids 翻译主键集合
     * @param other 其他 拓展字段
     * @return
     */
    public Map<String, String> batchFindByTypeAndCodes(String type, Set<String> ids, String other) {
        Map<String, Object> cacheMapAll = NamespaceCacheUtil.getAll(type);
        Map<String, String> result = new HashMap<>();
        List<String> keys = new ArrayList<>(ids);
        if (cacheMapAll == null) {
            NamespaceCacheUtil.configNamespace(type, 10000, 10, TimeUnit.MINUTES);
        } else {
            result.putAll(cacheMapAll.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().toString()
                )));
            keys = ids.stream()
                .filter(element -> !cacheMapAll.containsKey(element)).toList();
            if (!CollectionUtil.isEmpty(keys)) {
                TranslationInterface<?> translator = TRANSLATION_MAPPER.get(type);
                if (translator != null) {
                    try {
                        Map<String, String> noCacheMap = translator.batchTranslation(
                            new ArrayList<>(keys), other);
                        // 調用接口
                        if (CollectionUtil.isNotEmpty(noCacheMap)) {
                            result.putAll(noCacheMap);
                            // 新增缓存
                            for (String key : noCacheMap.keySet()) {
                                NamespaceCacheUtil.put(type, key, noCacheMap.get(key));
                            }
                        }

                    } catch (Exception e) {
                        log.error("Real-time translation failed: {}/{}", type, keys, e);

                    }
                }

            }

        }
        return result;
    }


}
