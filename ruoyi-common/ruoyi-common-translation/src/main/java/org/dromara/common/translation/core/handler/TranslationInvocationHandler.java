package org.dromara.common.translation.core.handler;


import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.core.cache.TranslationCache;
import org.dromara.common.translation.core.context.TranslationContext;

/**
 * 翻译调用处理器，用于处理对象及其嵌套结构的翻译逻辑。
 *  纯反射实现  递归获取 map List 对象数组  对象
 * @author zc
 *
 */
@RequiredArgsConstructor
@Slf4j
public class TranslationInvocationHandler {

    /**
     * 返回对象
     */
    private final Object target;

    /**
     * 数据处理服务
     */
    private final TranslationCache translationCache;

    /**
     * 项目组织前缀
     *  扫描  返回参数中  对本项目的 对象 进行递归 扫描翻译注解 防止 java  反射 越权报错
     */
    private final  String translationPackage;

    /**
     * 启动翻译处理流程
     *
     * @return 处理后的对象，若target为null则返回null
     */
    public Object start() {
        if (target == null) {
            return null;
        }
        return processResult(target);
    }

    /**
     * 根据对象类型进行路由处理
     *
     * @param result 待处理的对象
     * @return 处理后的对象
     */
    private Object processResult(Object result) {
        if (result instanceof Collection) {
            return batchProcessCollection((Collection<?>) result);
        } else if (result.getClass().isArray()) {
            return Arrays.stream((Object[]) result)
                .map(this::processSingle)
                .toArray();
        }
        return processSingle(result);
    }

    /**
     * 批量处理集合中的对象
     *
     * @param collection 待处理的集合
     * @return 处理后的集合
     */
    private Collection<?> batchProcessCollection(Collection<?> collection) {
        if (collection.isEmpty()) {
            return collection;
        }
        List<TranslationContext> contexts = new ArrayList<>();
        collection.forEach(obj -> collectTranslationContexts(obj, contexts));
        this.batchProcess(contexts);
        contexts.forEach(TranslationContext::applyTranslation);
        return collection;
    }

    /**
     * 处理单个对象
     *
     * @param obj 待处理的单个对象
     * @return 处理后的对象
     */
    private Object processSingle(Object obj) {
        if (obj == null) {
            return null;
        }
        List<TranslationContext> contexts = new ArrayList<>();
        collectTranslationContexts(obj, contexts);
        this.batchProcess(contexts);
        contexts.forEach(TranslationContext::applyTranslation);
        return obj;
    }

    /**
     * 递归收集需要翻译的字段
     *
     * @param obj      待扫描的对象
     * @param contexts 翻译上下文集合
     */
    private void collectTranslationContexts(Object obj, List<TranslationContext> contexts) {
        if (obj == null || !isJavaBuiltInType(obj.getClass())) {
            return;
        }
        if (obj instanceof Collection) {
            ((Collection<?>) obj).forEach(item -> collectTranslationContexts(item, contexts));
            return;
        }

        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                processField(obj, field, contexts);
            } catch (IllegalAccessException e) {
                log.error("Failed to access field: {}", field.getName(), e);
            }
        }
    }

    /**
     * 处理单个字段
     *
     * @param obj      包含字段的对象
     * @param field    待处理的字段
     * @param contexts 翻译上下文集合
     * @throws IllegalAccessException 如果字段访问失败
     */
    private void processField(Object obj, Field field, List<TranslationContext> contexts)
        throws IllegalAccessException {
        Object fieldValue = field.get(obj);

        if (field.isAnnotationPresent(Translation.class)) {
            processTranslationAnnotation(obj, field, contexts);
        }

        if (fieldValue != null && isJavaBuiltInType(fieldValue.getClass())
            && !TranslationContext.isListOfString(field)) {
            processNestedField(fieldValue, contexts);
        }
    }

    /**
     * 处理带有@Translation注解的字段
     *
     * @param obj      包含字段的对象
     * @param field    带有注解的字段
     * @param contexts 翻译上下文集合
     */
    private void processTranslationAnnotation(Object obj, Field field, List<TranslationContext> contexts) {
        Translation annotation = field.getAnnotation(Translation.class);
        if (TranslationContext.isListOfString(field) || TranslationContext.isOfString(field)) {
            contexts.add(new TranslationContext(obj, field, annotation, null));
        }
    }

    /**
     * 处理嵌套字段
     *
     * @param fieldValue 字段值
     * @param contexts   翻译上下文集合
     */
    private void processNestedField(Object fieldValue, List<TranslationContext> contexts) {
        if (fieldValue instanceof Map<?, ?>) {
            processMapField((Map<?, ?>) fieldValue, contexts);
        } else if (fieldValue instanceof Collection) {
            ((Collection<?>) fieldValue).forEach(item -> collectTranslationContexts(item, contexts));
        } else if (fieldValue.getClass().isArray()) {
            Arrays.stream((Object[]) fieldValue).forEach(item -> collectTranslationContexts(item, contexts));
        } else {
            collectTranslationContexts(fieldValue, contexts);
        }
    }

    /**
     * 处理Map类型的字段
     *
     * @param map      Map对象
     * @param contexts 翻译上下文集合
     */
    private void processMapField(Map<?, ?> map, List<TranslationContext> contexts) {
        map.values().forEach(value -> {
            if (value instanceof Collection) {
                ((Collection<?>) value).forEach(item -> collectTranslationContexts(item, contexts));
            } else if (value != null && value.getClass().isArray()) {
                Arrays.stream((Object[]) value).forEach(item -> collectTranslationContexts(item, contexts));
            } else if (value != null && isJavaBuiltInType(value.getClass())) {
                collectTranslationContexts(value, contexts);
            }
        });

    }

    /**
     * 判断是否为需要处理的类型
     *
     * @param clazz 待检查的类
     * @return 如果是需要处理的类型返回true
     */
    private boolean isJavaBuiltInType(Class<?> clazz) {
        return clazz.getName().startsWith("java.util.List")
            || clazz.getName().startsWith(translationPackage)
            || clazz.getName().startsWith("java.util.HashMap") || clazz.getName().startsWith("java.util.ArrayList");
    }


    /**
     * 将翻译后结果 存储到上下文中
     * @param contexts 上下文集合
     */
    public void batchProcess(List<TranslationContext> contexts) {
        if (contexts.isEmpty()) {
            return;
        }
        contexts.stream()
            .collect(Collectors.groupingBy(TranslationContext::getAnnotation))
            .forEach((annotation, ctxList) -> {
                Set<String> keys = ctxList.stream()
                    .map(TranslationContext::getSourceValue)
                    .filter(Objects::nonNull)
                    .flatMap(sourceValue ->
                        sourceValue instanceof List ? ((List<?>) sourceValue).stream()
                            : Stream.of(sourceValue))
                    .map(String::valueOf)
                    .collect(Collectors.toSet());

                if (keys.isEmpty()) {
                    return;
                }
                Map<String, String> translations = translationCache
                    .batchFindByTypeAndCodes(annotation.type(), keys, annotation.other());

                ctxList.forEach(ctx -> {
                    Object translatedValue = TranslationContext.isListOfString(ctx.getTargetField())
                        ? ((List<?>) ctx.getSourceValue()).stream()
                        .map(o -> translations.get(String.valueOf(o)))
                        .collect(Collectors.toList())
                        : translations.get(String.valueOf(ctx.getSourceValue()));
                    if (translatedValue != null) {
                        try {
                            ctx.setTranslatedValue(translatedValue);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            });
    }

}
