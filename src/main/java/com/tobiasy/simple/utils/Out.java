package com.tobiasy.simple.utils;

import java.util.List;

/**
 * @author tobiasy
 * @date 2019/7/19
 */
public class Out {
    public static void println(String message, Object... args) {
        String format = String.format(message, args);
        System.out.println(format);
    }

    public static <T> void println(T t) {
        System.out.println(t);
    }

    public static <T> void println(List<T> ts) {
        ts.forEach(System.out::println);
    }
}
