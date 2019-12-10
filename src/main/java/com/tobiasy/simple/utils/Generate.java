package com.tobiasy.simple.utils;

import com.tobiasy.simple.enums.BeanPrefix;

/**
 * @author tobiasy
 * @date 2019/7/9
 */
public class Generate {
    /**
     * 获取属性名称对应的Get方法名称
     *
     * @param fieldName 属性名称
     * @return
     */
    public static String toGetter(String fieldName) {
        String get = BeanPrefix.GET.getKey();
        return get + StringUtils.capitalize(fieldName);
    }

    /**
     * 获取属性名称对应的Set方法名称
     *
     * @param fieldName 属性名称
     * @return
     */
    public static String toSetter(String fieldName) {
        String set = BeanPrefix.SET.getKey();
        return set + StringUtils.capitalize(fieldName);
    }

    public static String toIsPredicate(String fieldName) {
        return BeanPrefix.IS.getKey() + StringUtils.capitalize(fieldName);
    }

    public static String toHasPredicate(String fieldName) {
        return BeanPrefix.HAS.getKey() + StringUtils.capitalize(fieldName);
    }
}

