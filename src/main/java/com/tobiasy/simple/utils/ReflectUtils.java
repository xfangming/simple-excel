package com.tobiasy.simple.utils;


import com.tobiasy.simple.bean.ModifierEnum;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author tobiasy
 */
public class ReflectUtils {
    private static final String MODIFIERS_METHOD_NAME = "getModifiers";

    /**
     * 获取当前Class中的所有属性
     *
     * @param clazz Class
     * @return 属性集合
     */
    public static List<Field> getDeclaredFields(Class clazz) {
        Field[] targetField = clazz.getDeclaredFields();
        List<Field> fields = Arrays.asList(targetField);
        return new ArrayList<>(fields);
    }

    /**
     * 获取class及其父类所有属性
     *
     * @param clazz Class对象
     * @return 属性集合
     */
    public static List<Field> getGlobalFields(Class clazz) {
        List<Field> fieldList = getDeclaredFields(clazz);
        Class superclass = clazz.getSuperclass();
        if (superclass != null) {
            fieldList.addAll(getGlobalFields(superclass));
        }
        return fieldList;
    }

    /**
     * 按照属性名称检索当前对象中的属性域
     *
     * @param clazz     当前对象
     * @param fieldName 属性名称
     * @return Field
     */
    public static Field findDeclaredField(Class clazz, String fieldName) throws NoSuchFieldException {
        return clazz.getDeclaredField(fieldName);
    }

    /**
     * 根据属性名fieldName查找指定类clazz及其父类中的公共属性域Field
     *
     * @param clazz     Class对象
     * @param fieldName 属性名称
     * @return Field
     */
    public static Field findField(Class clazz, String fieldName) {
        try {
            return clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * 根据属性名fieldName查找指定类clazz或者父类中的属性域Field
     *
     * @param clazz     Class
     * @param fieldName 属性名
     * @return Field
     */
    public static Field findGlobalField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException var4) {
            return clazz.getSuperclass() != null ? findGlobalField(clazz.getSuperclass(), fieldName) : null;
        }
    }

    /**
     * 按照枚举类型检索class中满足条件的的属性
     *
     * @param clazz        Class
     * @param modifierEnum 枚举
     * @return 属性集合
     */
    public static List<Field> getDeclaredFieldsByModifier(Class<?> clazz, ModifierEnum... modifierEnum) {
        List<Field> fieldList = getDeclaredFields(clazz);
        return filtrateModifier(fieldList, (b) -> b, modifierEnum);
    }

    /**
     * 不包含出现的 修饰符合集
     *
     * @param clazz        类
     * @param modifierEnum 修饰符
     * @return List<Field>
     */
    public static List<Field> getDeclaredFieldsOutModifier(Class<?> clazz, ModifierEnum... modifierEnum) {
        List<Field> fieldList = getDeclaredFields(clazz);
        return filtrateModifier(fieldList, (b) -> !b, modifierEnum);
    }

    /**
     * 不包含出现的 每一个修饰符
     *
     * @param clazz        类
     * @param modifierEnum 修饰符
     * @return List<Field>
     */
    public static List<Field> getDeclaredFieldsNoModifier(Class<?> clazz, ModifierEnum... modifierEnum) {
        List<Field> fieldList = getDeclaredFields(clazz);
        return filtrateOrModifier(fieldList, (b) -> !b, modifierEnum);
    }

    /**
     * 全局检索满足条件的属性
     *
     * @param clazz        Class对象
     * @param modifierEnum 修饰符枚举
     * @return 属性集合
     */
    public static List<Field> getGlobalFieldsByModifier(Class clazz, ModifierEnum... modifierEnum) {
        List<Field> fieldList = getDeclaredFieldsByModifier(clazz, modifierEnum);
        Class supperClass = clazz.getSuperclass();
        if (supperClass != null) {
            fieldList.addAll(getGlobalFieldsByModifier(supperClass, modifierEnum));
        }
        return fieldList;
    }

    public static List<Field> getGlobalFieldsOutModifier(Class clazz, ModifierEnum... modifierEnum) {
        List<Field> fieldList = getDeclaredFieldsOutModifier(clazz, modifierEnum);
        Class supperClass = clazz.getSuperclass();
        if (supperClass != null) {
            fieldList.addAll(getGlobalFieldsOutModifier(supperClass, modifierEnum));
        }
        return fieldList;
    }


    /*
     * *******************************   Method   **************************************
     */

    /**
     * 执行静态方法（一个参数）
     *
     * @param clazz         Class对象
     * @param methodName    方法名称
     * @param parameterType 参数类型
     * @param value         参数值
     * @return Object
     */
    public static Object invokeStatic(Class<?> clazz, String methodName, Class<?> parameterType, Object value) {
        Class<?>[] parameterTypes = new Class[]{parameterType};
        Object[] values = new Object[]{value};
        return invokeStatic(clazz, methodName, parameterTypes, values);
    }

    public static Object invokeStatic(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object[] values) {
        Object value;
        try {
            Method method = clazz.getMethod(methodName, parameterTypes);
            value = method.invoke(null, values);
            return value;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取全局范围内的公共方法
     *
     * @param clazz Class
     * @return 方法集合
     */
    public static List<Method> getMethods(Class clazz) {
        return Arrays.asList(clazz.getMethods());
    }

    /**
     * 获取当前Class对象中的所有方法
     *
     * @param clazz Class对象
     * @return 方法集合
     */
    public static List<Method> getDeclaredMethods(Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredMethods());
    }

    /**
     * 获取所有的方法
     *
     * @param clazz Class对象
     * @return 方法集合
     */
    public static List<Method> getGlobalMethods(Class<?> clazz) {
        List<Method> methods = getDeclaredMethods(clazz);
        Class superclass = clazz.getSuperclass();
        if (superclass != null) {
            methods.addAll(getGlobalMethods(superclass));
        }
        return methods;
    }

    /**
     * 获取方法域
     *
     * @param clazz      Class
     * @param methodName 方法名称
     * @param paramTypes Method类型
     * @return Method
     */
    public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * 按照方法名称在当前对象及其所有父类对象中进行检索
     *
     * @param clazz      对象
     * @param methodName 方法名称
     * @param paramTypes 参数类型
     * @return Method
     */
    public static Method findGlobalMethod(Class<?> clazz, String methodName, Class... paramTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ex) {
            return clazz.getSuperclass() != null ? findGlobalMethod(clazz.getSuperclass(), methodName, paramTypes) : null;
        }
    }

    /**
     * 获取Class中满足枚举类型条件的方法列表
     *
     * @param clazz        Class对象
     * @param modifierEnum 枚举类型
     * @return 方法集合
     */
    public static List<Method> getDeclaredMethodsByModifier(Class clazz, ModifierEnum... modifierEnum) {
        List<Method> methods = getDeclaredMethods(clazz);
        return filtrateModifier(methods, (b) -> b, modifierEnum);
    }

    /**
     * 全局检索满足枚举修饰类型条件的方法（含父类）
     *
     * @param clazz        Class对象
     * @param modifierEnum 修饰符类型枚举
     * @return 方法集合
     */
    public static List<Method> getGlobalMethodsByModifier(Class clazz, ModifierEnum... modifierEnum) {
        List<Method> methods = getGlobalMethods(clazz);
        return filtrateModifier(methods, (b) -> b, modifierEnum);
    }

    /**
     * @param list          传入进行检索的属性列表或者方法列表
     * @param satisfiedFunc 是否满足枚举类型函数
     * @param modifierEnum  修饰符枚举
     * @return 属性列表或者方法列表
     */
    private static <T> List<T> filtrateModifier(List<T> list,
                                                Function<Boolean, Boolean> satisfiedFunc,
                                                ModifierEnum... modifierEnum) {
        List<T> resultList = new ArrayList<>();
        for (T t : list) {
            int modifiers = (Integer) ReflectUtils.invoke(t, MODIFIERS_METHOD_NAME);
            boolean modifier = isModifier(modifiers, (b) -> !b, true, modifierEnum);
            if (satisfiedFunc.apply(modifier) && !resultList.contains(t)) {
                resultList.add(t);
            }
        }
        return resultList;
    }

    private static <T> List<T> filtrateOrModifier(List<T> list,
                                                  Function<Boolean, Boolean> satisfiedFunc,
                                                  ModifierEnum... modifierEnum) {
        List<T> resultList = new ArrayList<>();
        for (T t : list) {
            int modifiers = (Integer) ReflectUtils.invoke(t, MODIFIERS_METHOD_NAME);
            boolean orModifier = isModifier(modifiers, (b) -> b, false, modifierEnum);
            if (satisfiedFunc.apply(orModifier) && !resultList.contains(t)) {
                resultList.add(t);
            }
        }
        return resultList;
    }

    /**
     * 判断modifiers值是否满足枚举类型
     *
     * @param modifiers Method对象或Field对象中的getModifiers方法
     * @param modifier  修饰符枚举
     * @return Boolean
     */
    public static Boolean isModifier(int modifiers,
                                     Function<Boolean, Boolean> function,
                                     boolean defaultValue,
                                     ModifierEnum... modifier) {
        boolean isModifier;
        for (ModifierEnum modifierEnum : modifier) {
            String name = Generate.toIsPredicate(modifierEnum.name().toLowerCase());
            isModifier = (Boolean) invokeStatic(Modifier.class, name, int.class, modifiers);
            Boolean res = function.apply(isModifier);
            if (res) {
                return isModifier;
            }
        }
        return defaultValue;
    }

    /**
     * 根据方法对象（Method）或者属性对象（Field）获取修饰符
     *
     * @param modifiers 对象的 modifiers 值
     * @return 修饰符枚举集合
     */
    public static List<ModifierEnum> getEnumByModifiers(Integer modifiers) {
        List<ModifierEnum> list = new ArrayList<>();
        for (ModifierEnum modifier : ModifierEnum.values()) {
            String name = Generate.toIsPredicate(modifier.name().toLowerCase());
            Boolean flag = (Boolean) ReflectUtils.invokeStatic(Modifier.class, name, int.class, modifiers);
            if (flag) {
                list.add(modifier);
            }
        }
        return list;
    }

    /**
     * 获取当前类和父类的所有public的方法
     *
     * @param clazz      对象
     * @param methodName 方法名称
     * @param paramTypes 参数类型
     * @return Method
     */
    public static Method findMethod(Class<?> clazz, String methodName, Class... paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行一个无参方法
     *
     * @param obj        操作对象
     * @param methodName 属性名
     * @return Object 结果
     * @throws RuntimeException;
     */
    public static Object invoke(Object obj, String methodName) throws RuntimeException {
        if (obj == null || methodName == null) {
            return null;
        }
        Object value = null;
        try {
            Method getMethod = obj.getClass().getMethod(methodName);
            if (getMethod == null) {
                return null;
            }
            getMethod.setAccessible(true);
            value = getMethod.invoke(obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 跳过不存在的方法
     *
     * @param obj        对象
     * @param methodName 方法名称
     * @return Object
     */
    public static Object invokeSkipError(Object obj, String methodName) {
        if (obj == null || methodName == null) {
            return null;
        }
        Object value = null;
        try {
            Method method = findGlobalMethod(obj.getClass(), methodName);
            if (method != null) {
                method.setAccessible(true);
                value = method.invoke(obj);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
        return value;
    }

    /**
     * 一个参数
     *
     * @param object     invoke对象
     * @param methodName 方法名称
     * @param paramType  参数类型
     * @param parameter  参数
     * @return Object
     */
    public static Object invokeByType(Object object, String methodName, Class paramType, Object parameter) {
        Class[] paramTypes = {paramType};
        Object[] parameters = {parameter};
        return invoke(object, methodName, paramTypes, parameters);
    }

    /**
     * 利用反射执行类中指定方法，多个参数
     *
     * @param object     类对象
     * @param methodName 方法名称
     * @param paramTypes 参数类型
     * @param parameters 参数
     * @return Object
     */
    public static Object invokeByType(Object object, String methodName, Class[] paramTypes, Object[] parameters) {
        Class<?> clazz = object.getClass();
        Method method = findMethod(clazz, methodName, paramTypes);
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 一个参数自动转型
     *
     * @param object     操作对象
     * @param methodName 方法名
     * @param paramType  参数类型
     * @param parameter  参数
     * @return Object
     */
    public static Object invoke(Object object, String methodName, Class paramType, Object parameter) {
        return invokeByType(object, methodName, paramType, parameter);
    }

    /**
     * 根据类型自动转型
     *
     * @param object     操作对象
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @param parameters 参数
     * @return Object
     */
    public static Object invoke(Object object, String methodName, Class[] paramTypes, Object[] parameters) {
        for (int i = 0; i < paramTypes.length; i++) {
            parameters[i] = ClassParser.getType(parameters[i], paramTypes[i]);
        }
        return invokeByType(object, methodName, paramTypes, parameters);
    }

}
