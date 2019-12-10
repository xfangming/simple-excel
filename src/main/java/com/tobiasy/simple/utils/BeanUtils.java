package com.tobiasy.simple.utils;


import com.tobiasy.simple.bean.ModifierEnum;
import com.tobiasy.simple.exception.OperationException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

import static com.tobiasy.simple.utils.Generate.toGetter;
import static com.tobiasy.simple.utils.ReflectUtils.*;


/**
 * @author tobiasy
 */
public class BeanUtils {

    /**
     * 对象复制，支持对象内部对象属性复制
     *
     * @param source 对象源
     * @param target 对象目标
     * @return Long 用时
     */
    public static Long copyProperties(Object source, Object target) {
        long start = System.currentTimeMillis();
        if (source == null) {
            return System.currentTimeMillis() - start;
        }
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        List<Field> sourceField = ReflectUtils.getGlobalFieldsOutModifier(sourceClass, ModifierEnum.STATIC);
        List<Field> targetField = ReflectUtils.getGlobalFieldsOutModifier(targetClass, ModifierEnum.STATIC);
        for (Field field : sourceField) {
            String fieldName = field.getName();
            for (Field trg : targetField) {
                String fieldName1 = trg.getName();
                boolean f = field.getType().getName().equals(trg.getType().getName());
                if (fieldName.equals(fieldName1) && f) {
                    try {
                        Object s1 = getProperty(source, fieldName);
                        if (s1 != null) {
                            setProperty(target, fieldName, s1);
                        }
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return System.currentTimeMillis() - start;
    }

    /**
     * 给object对象中的fieldName属性赋值value
     * 条件是fieldName属性没有set()方法
     *
     * @param object    对象
     * @param fieldName 属性名称
     * @param value     赋值value
     * @return Object
     */
    public static Object setProperty(Object object, String fieldName, Object value) {
        Field field = findGlobalField(object.getClass(), fieldName);
        if (field == null) {
            return object;
        }
        Object v = ClassParser.getType(value, field.getType());
        field.setAccessible(true);
        try {
            field.set(object, v);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static Object getProperty(Class clazz, String fieldName) {
        Field field = findGlobalField(clazz, fieldName);
        if (field == null) {
            return null;
        }
        field.setAccessible(true);
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getProperty(Object object, String fieldName) {
        Field field = findGlobalField(object.getClass(), fieldName);
        if (field == null) {
            return null;
        }
        return getProperty(object, field);
    }

    public static Object getProperty(Object object, Field field) {
        if (field == null) {
            return null;
        }
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 三元运算符简写
     *
     * @param object       判断对象
     * @param defaultValue 对象为null时的默认值
     * @return
     */
    public static Object ternary(Object object, Object notNullValue, Object defaultValue) {
        return object != null ? notNullValue : defaultValue;
    }

    public static Object ternary(Object object, Object defaultValue) {
        return ternary(object, object, defaultValue);
    }

    public static Object ternary(Object object) {
        return ternary(object, null);
    }

    public static <T> List<T> toArrayList(T t) {
        List<T> ts = new ArrayList<>();
        ts.add(t);
        return ts;
    }

    /**
     * 判断一个对象是否是基本类型或基本类型的封装类型
     * 原始类型（boolean、char、byte、short、int、long、float、double）
     *
     * @param obj
     * @return
     */
    public static boolean isPrimitive(Object obj) {
        try {
            Class clazz = obj.getClass();
            Field type = clazz.getField("TYPE");
            Object o = type.get(null);
            return ((Class<?>) o).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 流的关闭操作
     *
     * @param obj 流对象
     */
    public static void close(Object obj) {
        if (obj != null) {
            try {
                ReflectUtils.invoke(obj, "close");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public static <T> Map<String, Object> toMap(T obj) {
        Map<String, Object> map;
        try {
            if (obj == null) {
                return new HashMap<>(1);
            } else {
                Field[] fields = obj.getClass().getDeclaredFields();
                map = new HashMap<>(fields.length);
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object o = field.get(obj);
                    if (o != null) {
                        map.put(field.getName(), o);
                    }
                }
                return map;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new HashMap<>(1);
    }

    public static <T> List<Map<String, Object>> toMapList(List<T> list) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        } else {
            List<Map<String, Object>> maps = new ArrayList<>(list.size());
            Iterator var2;
            for (var2 = list.iterator(); var2.hasNext(); ) {
                Object tem = var2.next();
                maps.add(toMap(tem));
            }

            return maps;
        }
    }

    public static <T> Map<String, Object> toMap(T t, String[] attrs) {
        Class<?> clazz = t.getClass();
        clazz.getDeclaredFields();
        Map<String, Object> map = new HashMap<>(attrs.length);
        for (String attr : attrs) {
            Object value = ReflectUtils.invoke(t, toGetter(attr));
            map.put(StringUtils.underline(attr), value);
        }
        return map;
    }

    public static <T, R> R doFieldNotNull(T t, Function<T, R> function, String msg) {
        R r = doIfNotNull(t, function);
        if (r == null) {
            throw new OperationException(msg);
        }
        return r;
    }

    public static <T, R> R doIfNotNull(T t, Function<T, R> function) {
        if (t != null) {
            return function.apply(t);
        } else {
            return null;
        }
    }

    public static <T> T doNotNull(T t, T next){
        if (t == null) {
            return next;
        }
        return t;
    }

    /**
     * 根据字段值是否为空 动态生成对象的非空toString
     *
     * @param t      对象
     * @param fields 属性
     * @param <T>    对象泛型
     * @return String
     */
    public static <T> String toString(T t, String[] fields) {
        String simpleName = t.getClass().getSimpleName();
        final StringBuilder sb = new StringBuilder(simpleName);
        sb.append("{");
        boolean notFirst = false;
        for (String param : fields) {
            Object result = ReflectUtils.invoke(t, toGetter(param));
            if (result != null) {
                if (result instanceof Object[]) {
                    Object[] types = (Object[]) result;
                    if (types.length > 0) {
                        if (notFirst) {
                            sb.append(", ");
                        }
                        sb.append(param).append("=[");
                        for (int j = 0; j < types.length; j++) {
                            if (j > 0) {
                                sb.append(", ");
                            }
                            sb.append(types[j]);
                        }
                        sb.append("]");
                        notFirst = true;
                    }
                } else {
                    if (notFirst) {
                        sb.append(", ");
                    }
                    sb.append(param).append("=").append(result);
                    notFirst = true;
                }
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public static <T> String toString(T t) {
        Class<?> clazz = t.getClass();
        List<Field> globalFields = ReflectUtils.getGlobalFieldsOutModifier(
                clazz,
                ModifierEnum.STATIC, ModifierEnum.FINAL);
        String[] collect = globalFields.stream().map(Field::getName).toArray(String[]::new);
        return toString(t, collect);
    }

}
