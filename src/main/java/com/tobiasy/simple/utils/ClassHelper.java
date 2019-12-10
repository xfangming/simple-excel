package com.tobiasy.simple.utils;

import java.util.Collection;

/**
 * @author tobiasy
 * @date 2019/9/29
 */
public class ClassHelper {


    /**
     * 获取集合泛型Class
     *
     * @param list 集合
     * @param <T>
     * @return
     */
    public static <T> Class<?> getClass(Collection<T> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (T t : list) {
                if (t != null) {
                    return t.getClass();
                }
            }
        }
        return null;
    }
}


