package com.tobiasy.simple.anno;

/**
 * @author tobiasy
 * @date 2019/7/17
 */
@FunctionalInterface
public interface TwinParamFunction<M, N, R> {
    /**
     * 双参数带返回值函数
     * @param m
     * @param n
     * @return
     */
    R apply(M m, N n);
}
