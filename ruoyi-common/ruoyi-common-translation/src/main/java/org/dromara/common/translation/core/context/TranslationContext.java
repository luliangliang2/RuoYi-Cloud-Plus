package org.dromara.common.translation.core.context;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.reflect.ReflectUtils;
import org.dromara.common.translation.annotation.Translation;

@Data
@AllArgsConstructor
public class TranslationContext {

    private Object targetObject;
    private Field targetField;
    private Translation annotation;
    private Object translatedValue;

    /**
     * 获取需要翻译的原始值
     */
    public Object getSourceValue() {
        String mapper = annotation.mapper();
        if (StringUtils.isNotBlank(mapper)) {
            return ReflectUtils.getFieldValue(targetObject, mapper);
        }
        return ReflectUtils.getFieldValue(targetObject, targetField.getName());
    }

    /**
     * 检查字段是否是 List<String> 类型
     *
     * @param field 要检查的字段
     * @return 如果是 List<String> 返回 true，否则返回 false
     */
    public static boolean isListOfString(Field field) {
        // 1. 首先检查基本类型是否是 List 或其子类
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }
        // 2. 检查泛型类型参数
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType parameterizedType) {
            // 获取泛型的实际类型参数
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

            // 如果是 List<String>，应该只有一个类型参数且是 String.class
            return actualTypeArguments.length == 1
                && actualTypeArguments[0] == String.class;
        }
        return false;
    }

    /**
     * 检查字段是否是 String 类型
     *
     * @param field 要检查的字段
     * @return 如果是  String 返回 true，否则返回 false
     */
    public static boolean isOfString(Field field) {
        return String.class.isAssignableFrom(field.getType());
    }


/**
 * 设置翻译后的值
 */
public void setTranslatedValue(Object value) throws IllegalAccessException {
    // 存储翻译后的值
    this.translatedValue = value;
}

/**
 * 应用翻译结果到目标字段
 */
public void applyTranslation() {
    if (translatedValue == null) {
        return;
    }

    try {
        // 只翻译 List<String> 和 String 类型
        if (isListOfString(targetField) ||
            String.class.isAssignableFrom(targetField.getType())) {
            targetField.setAccessible(true);
            targetField.set(targetObject, translatedValue);
        }
    } catch (IllegalAccessException e) {
        throw new RuntimeException(
            "Failed to apply translation to field: " + targetField.getName(), e);
    }
}
}
