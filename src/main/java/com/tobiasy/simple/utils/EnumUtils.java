package com.tobiasy.simple.utils;


import com.tobiasy.simple.exception.OperationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * @author tobiasy
 * @date 2018/12/14
 */
public class EnumUtils {
    /**
     * 通过枚举名称初始化
     *
     * @param function 获取所有枚举函数
     * @param name     枚举名称
     * @return
     */
    public static Enum nameInstance(Supplier<Enum[]> function, String name) {
        Enum[] enumsArr = function.get();
        for (Enum anEnum : enumsArr) {
            if (anEnum.name().equals(name)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 默认用来获取枚举对象的属性
     */
    private static final String DEFAULT_ATTR = "value";

    /**
     * 通过属性和属性值获取枚举对象
     *
     * @param enumType 枚举类型
     * @param attr     属性
     * @param value    属性值
     * @return
     */
    public static <T> Enum getEnumObject(Class<T> enumType, String attr, Object value) {
        try {
            Method method = ReflectUtils.findMethod(enumType, "values", null);
            if (method == null) {
                throw new OperationException("%s中没有找到values()方法！", enumType);
            }
            Enum[] enums = (Enum[]) method.invoke(null);
            for (Enum e : enums) {
                Object o = ReflectUtils.invoke(e, Generate.toGetter(attr));
                if (o.equals(value)) {
                    return e;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过默认属性和属性值获取枚举对象
     *
     * @param enumType
     * @param value
     * @return
     */
    public static <T> Enum getEnumByAttr(Class<T> enumType, String value) {
        return getEnumObject(enumType, DEFAULT_ATTR, value);
    }

    public static Enum getEnumByAttr(Class<?> enumType, String attr, Object value) {
        return getEnumObject(enumType, attr, value);
    }

    /**
     * 获取枚举类型中的某个字段对应的所有值
     *
     * @param type 枚举类型
     * @param attr 枚举类型中对应的属性
     * @return
     */
    public static String getEnumValue(Class<?> type, String attr) {
        StringBuilder buffer = new StringBuilder();
        try {
            Method values = type.getMethod("values");
            Enum[] es = (Enum[]) values.invoke(null);
            boolean isFirst = true;
            for (Enum e : es) {
                String methodName = null;
                if (attr != null) {
                    methodName = Generate.toGetter(attr);
                }
                Object obj = ReflectUtils.invoke(e, methodName);
                if (isFirst) {
                    isFirst = false;
                } else {
                    buffer.append(",");
                }
                if (obj == null) {
                    buffer.append(e.name());
                } else {
                    buffer.append(obj);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new OperationException(e.getMessage());
        }
        return buffer.toString();
    }
} 