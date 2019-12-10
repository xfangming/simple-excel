/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package com.tobiasy.simple.anno;


/**
 * @author tobiasy
 */
@FunctionalInterface
public interface ImportFunction<T,R> {

    /**
     * 用于excel导入中自定义函数辅助函数调用
     * @param s 自定义枚举中的显示字段
     * @param clazz 枚举对象类型
     * @return R
     */
    R toEnum(T s, Class clazz);
}
