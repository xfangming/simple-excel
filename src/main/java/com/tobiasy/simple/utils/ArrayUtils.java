package com.tobiasy.simple.utils;

import com.tobiasy.simple.constants.SymbolConst;
import com.tobiasy.simple.enums.ClassEnum;
import com.tobiasy.simple.enums.SortEnum;
import com.tobiasy.simple.exception.OperationException;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;

import static com.tobiasy.simple.utils.ReflectUtils.invoke;


/**
 * @author tobiasy
 */
public class ArrayUtils {

    public static <T> T getNotNullArray(T[] arrays, int index) {
        T current = arrays[index];
        if (current != null) {
            return current;
        } else {
            throw new OperationException("数组%s索引为%s的值不能为空！", arrays, index);
        }
    }

    public static <T, R> Function<T, R> getNotNullArray(Function<T, R>[] arrays, int index) {
        Function<T, R> current = arrays[index];
        if (current != null) {
            return current;
        } else {
            throw new OperationException("数组%s索引为%s的值不能为空！", arrays, index);
        }
    }

    public static <T> void print(T... ts) {
        StringBuilder stringBuilder = new StringBuilder(SymbolConst.BRACKET_PREFIX);
        for (int i = 0; i < ts.length; i++) {
            if (i != 0) {
                stringBuilder.append(SymbolConst.COMMA_);
            }
            stringBuilder.append(ts[i]);
        }
        stringBuilder.append(SymbolConst.BRACKET_SUFFIX);
        System.out.println(stringBuilder.toString());
    }

    @SafeVarargs
    public static <T> T[] asArray(T... ts) {
        return ts;
    }

    @SafeVarargs
    public static <T, R> Function<T, R>[] asArray(Function<T, R>... functions) {
        return functions;
    }

    public static <T> List<T> toList(T[] arr, T... ts) {
        List<T> list = new ArrayList<>(Arrays.asList(arr));
        list.addAll(Arrays.asList(ts));
        return list;
    }

    public static <T, K> List<K> toList(T[] arr, Function<T, K> function) {
        List<K> list = new ArrayList<>();
        for (T t : arr) {
            K apply = function.apply(t);
            list.add(apply);
        }
        return list;
    }

    /**
     * List 集合转化为 Object[] 数组
     *
     * @param list 集合
     * @return Object类型数组
     */
    public static <T, K> K[] toArray(List<T> list, Function<T, K> function, IntFunction<K[]> intFunction) {
        K[] result = intFunction.apply(list.size());
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            K apply = function.apply(t);
            result[i] = apply;
        }
        return result;
    }

    /**
     * 数组转化为其他类型数组
     *
     * @param ts          数组
     * @param function    转换函数
     * @param intFunction 初始化函数
     * @param <T>         原始类型
     * @param <K>         返回类型
     * @return
     */
    public static <T, K> K[] toArray(T[] ts, Function<T, K> function, IntFunction<K[]> intFunction) {
        K[] result = intFunction.apply(ts.length);
        for (int i = 0; i < ts.length; i++) {
            K apply = function.apply(ts[i]);
            result[i] = apply;
        }
        return result;
    }

    public static <T, K> K[] toArrayPlus(List<T> list, Class<K> clazz) {
        ClassEnum instance = ClassEnum.getInstance(clazz);
        return toArray(list, instance.getFunction(), instance.getIntFunction());
    }

    public static <T, K> K[] toArrayPlus(T[] ts, Class<K> clazz) {
        ClassEnum instance = ClassEnum.getInstance(clazz);
        return toArray(ts, instance.getFunction(), instance.getIntFunction());
    }

    /**
     * 集合转化为数组，需要先初始化集合中的类型
     *
     * @param list
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T[] toArray(List<T> list, T[] t) {
        if (list.size() != t.length) {
            try {
                throw new Exception("初始化数组：" + t.getClass() + "长度不匹配！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int i = 0;
        for (T o : list) {
            t[i++] = o;
        }
        return t;
    }

    /**
     * 集合转化为数组
     * <code>ArrayUtils.toArray(list, Integer[]::new)</code>
     *
     * @param list     集合
     * @param function 数组初始化函数
     * @param <T>      泛型
     * @return
     */
    public static <T> T[] toArray(List<T> list, IntFunction<T[]> function) {
        T[] arr = function.apply(list.size());
        return toArray(list, arr);
    }

    public static <T> boolean contains(T value, T[] ojs) {
        for (T o : ojs) {
            if (o.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static <T> List<T> listAddArray(List<T> list, T[] arr) {
        List<T> result = new ArrayList<>(list);
        result.addAll(Arrays.asList(arr));
        return result;
    }

    public static <T> T[] arrayAddList(T[] arr, List<T> list, IntFunction<T[]> intFunction) {
        List<T> result = new ArrayList<>(list);
        result.addAll(Arrays.asList(arr));
        return toArray(result, intFunction.apply(result.size()));
    }

    public static <T> T[] arrayAddList(T[] arr, List<T> list) {
        List<T> result = new ArrayList<>(list);
        result.addAll(Arrays.asList(arr));
        Class<?> aClass = ClassHelper.getClass(list);
        ClassEnum instance = ClassEnum.getInstance(aClass);
        IntFunction<T[]> intFunction = instance.getIntFunction();
        return toArray(result, intFunction.apply(result.size()));
    }

    /**
     * 数组中添加List集合
     *
     * @param arr    数组
     * @param list   List集合
     * @param result 预先定义的空结果数组
     * @param <T>
     * @return
     */
    public static <T> T[] arrayAddList(T[] arr, List<T> list, T[] result) {
        if (result.length != (arr.length + list.size())) {
            throw new OperationException(new Throwable(result.getClass() + "长度跟被复制长度不一致！"));
        }
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        for (int i = 0; i < list.size(); i++) {
            result[i + arr.length] = list.get(i);
        }
        return result;
    }

    /**
     * 数组默认排序 -- 升序
     *
     * @param arr
     */
    public static void sort(Integer[] arr) {
        sort(arr, SortEnum.ASC);
    }

    /**
     * 排列，需要指定升序还是降序
     *
     * @param arr
     * @param sort
     */
    public static void sort(Integer[] arr, SortEnum sort) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (SortEnum.DESC.name().equals(sort.name())) {
                    if (arr[i] < arr[j]) {
                        int temp = arr[i];
                        arr[i] = arr[j];
                        arr[j] = temp;
                    }
                } else {
                    if (arr[i] > arr[j]) {
                        int temp = arr[i];
                        arr[i] = arr[j];
                        arr[j] = temp;
                    }
                }
            }
        }
    }

    public static <T> T[] appendOne(T[] arr, T t, IntFunction<T[]> function) {
        T[] ts = asArray(t);
        return append(arr, ts, function);
    }

    public static <T> T[] append(T[] arr, T[] ts, IntFunction<T[]> function) {
        T[] result = function.apply(arr.length + ts.length);
        return append(arr, ts, result);
    }

    public static <T> T[] append(T[] arr, T[] ts, T[] result) {
        if (arr.length + ts.length != result.length) {
            throw new OperationException(new Throwable(result.getClass() + "结果集数组长度和子数组长度之和不一致！"));
        }
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            result[index++] = arr[i];
        }
        for (int i = 0; i < ts.length; i++) {
            result[index++] = ts[i];
        }
        return result;
    }

    /**
     * 从二维数组中运行指定的方法获取结果集
     *
     * @param params
     * @return
     */
    public static Object[] invoke2Demension(Object[][] params) {
        Object[] values = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            Object[] param = params[i];
            if (param[0] == null) {
                values[i] = param[1].toString();
            } else {
                values[i] = invoke(param[0], param[1].toString());
            }
        }
        return values;
    }

    /**
     * 过滤重复的键，把所有匹配的映射值放置到该键对应的值中
     *
     * @param keys
     * @param values
     * @return
     */
    public static Map<Object, List<Object>> truncateKey(Object[] keys, Object[] values) {
        Map<Object, List<Object>> map = new HashMap<>(keys.length);
        for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            List<Object> ss = map.get(key);
            if (ss == null) {
                ss = new ArrayList<>();
            }
            ss.add(values[i]);
            map.put(key, ss);
        }
        return map;
    }

    /**
     * 截短数组
     *
     * @param ts     操作数组
     * @param result 返回初始化数组
     * @param ignore 被忽略的下标数组
     * @param <T>    泛型
     * @return T
     */
    public static <T> T[] truncate(T[] ts, T[] result, Integer[] ignore) {
        return truncate(ts, ignore).toArray(result);
    }

    public static <T> Vector<T> truncate(T[] ts, Integer[] ignore) {
        Vector<T> vector = new Vector<>();
        for (int i = 0; i < ts.length; i++) {
            boolean b = contains(i, ignore);
            if (!b) {
                vector.add(ts[i]);
            }
        }
        return vector;
    }

    /**
     * 数组初始化
     *
     * @param intFunction 初始化函数
     * @param object      待放入数组中的对象
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> T[] getArray(IntFunction<T[]> intFunction,
                                   T... object) {
        T[] apply = intFunction.apply(object.length);
        for (int i = 0; i < object.length; i++) {
            apply[i] = object[i];
        }
        return apply;
    }

    @SafeVarargs
    public static Function<String, Object>[] getFunctionArray(IntFunction<Function<String, Object>[]> intFunction,
                                                              Function<String, Object>... object) {
        Function<String, Object>[] apply = intFunction.apply(object.length);
        for (int i = 0; i < object.length; i++) {
            apply[i] = object[i];
        }
        return apply;
    }

}
