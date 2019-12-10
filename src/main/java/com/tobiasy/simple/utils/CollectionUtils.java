package com.tobiasy.simple.utils;


import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;

/**
 * @author tobiasy
 * @date 2018/11/6
 */
public class CollectionUtils {
    private static Integer INTEGER_ONE = 1;

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean notEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    public static <T> Collection<T> of(Collection<T> a, Collection<T> b, IntBinaryOperator func) {
        List<T> list = new ArrayList<>();
        Map<Object, Integer> map1 = getCardinalityMap(a);
        Map<Object, Integer> map2 = getCardinalityMap(b);
        Set<T> set = new HashSet<>(a);
        set.addAll(b);
        Iterator<T> it;
        for (it = set.iterator(); it.hasNext(); ) {
            T obj = it.next();
            int i = 0;
            Integer res = func.applyAsInt(getFreq(obj, map1), getFreq(obj, map2));
            for (; i < res; ++i) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 2个数组取并集
     *
     * @param a
     * @param b
     * @return
     */
    public static <T> Collection<T> union(Collection<T> a, Collection<T> b) {
        return of(a, b, Math::max);
    }

    /**
     * 2个数组取交集
     *
     * @param a
     * @param b
     * @return
     */
    public static <T> Collection<T> intersection(Collection<T> a, Collection<T> b) {
        return of(a, b, Math::min);
    }

    /**
     * 2个数组取交集 的补集
     *
     * @param a
     * @param b
     * @return
     */
    public static <T> Collection<T> disjunction(Collection<T> a, Collection<T> b) {
        IntBinaryOperator function = (freq1, freq2) ->
                Math.max(freq1, freq2) - Math.min(freq1, freq2);
        return of(a, b, function);
    }

    /**
     * arrayA去除arrayB
     *
     * @param a
     * @param b
     * @return
     */
    public static <T> Collection<T> subtract(Collection<T> a, Collection<T> b) {
        List<T> list = new ArrayList<>(a);
        Iterator<T> it;
        for (it = b.iterator(); it.hasNext(); ) {
            list.remove(it.next());
        }
        return list;
    }

    private static Integer getFreq(Object obj, Map<Object, Integer> freqMap) {
        Integer count = freqMap.get(obj);
        return count != null ? count : 0;
    }

    public static <T> Map<Object, Integer> getCardinalityMap(Collection<T> coll) {
        Map<Object, Integer> count = new HashMap<>(coll.size());
        Iterator it;
        for (it = coll.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            Integer c = (count.get(obj));
            if (c == null) {
                count.put(obj, INTEGER_ONE);
            } else {
                count.put(obj, c + 1);
            }
        }
        return count;
    }

    /**
     * 根据集合某属性分组，转化为Map结构
     *
     * @param collection 集合
     * @param function   函数
     * @param <T>        泛型
     * @return
     */
    public static <T, R> Map<R, List<T>> groupList(Collection<T> collection, Function<T, R> function) {
        Map<R, List<T>> map = new HashMap<>(collection.size());
        for (T con : collection) {
            R apply = function.apply(con);
            List<T> o = map.get(apply);
            if (o == null) {
                o = new ArrayList<>();
                o.add(con);
                map.put(apply, o);
            } else {
                o.add(con);
            }
        }
        return map;
    }

    /**
     * 集合切割
     *
     * @param list 集合
     * @param size 每片大小
     * @param <T>  泛型
     */
    public static <T> List<List<T>> incise(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        double number = new BigDecimal(list.size()).divide(new BigDecimal(size), 0).doubleValue();
        double length = Math.ceil(number);
        for (int i = 0; i < length; i++) {
            List<T> list1 = new ArrayList<>();
            for (int j = i * size; j < (i + 1) * size; j++) {
                if (j < list.size()) {
                    list1.add(list.get(j));
                }
            }
            result.add(list1);
        }
        return result;
    }

} 