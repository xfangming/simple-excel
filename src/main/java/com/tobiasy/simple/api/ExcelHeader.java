package com.tobiasy.simple.api;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author tobiasy
 * @date 2019/11/11
 */
public class ExcelHeader<T, R> {
    private boolean isFunction = false;
    /**
     * 导出转换函数
     */
    private Function<T, R>[] functions;
    private String[] attrs;
    /**
     * 导入函数
     */
    private BiConsumer<T, String>[] attributes;

    public static <T, R> ExcelHeader<T, R> of(Function<T, R>[] functions) {
        return new ExcelHeader<T, R>().setFunctions(functions).setFunction(true);
    }

    public static <T, R> ExcelHeader<T, R> of(String[] attrs) {
        return new ExcelHeader<T, R>().setAttrs(attrs);
    }

    public static <T, R> ExcelHeader<T, R> of(BiConsumer<T, String>[] attributes) {
        return new ExcelHeader<T, R>().setAttributes(attributes).setFunction(true);
    }

    public static <T, R> ExcelHeader<T, R> empty() {
        return new ExcelHeader<>();
    }

    public boolean isFunction() {
        return isFunction;
    }

    public ExcelHeader<T, R> setFunction(boolean function) {
        isFunction = function;
        return this;
    }

    public Function<T, R>[] getFunctions() {
        return functions;
    }

    public ExcelHeader<T, R> setFunctions(Function<T, R>[] functions) {
        this.functions = functions;
        return this;
    }

    public String[] getAttrs() {
        return attrs;
    }

    public ExcelHeader<T, R> setAttrs(String[] attrs) {
        this.attrs = attrs;
        return this;
    }

    public BiConsumer<T, String>[] getAttributes() {
        return attributes;
    }

    public ExcelHeader<T, R> setAttributes(BiConsumer<T, String>[] attributes) {
        this.attributes = attributes;
        return this;
    }
}
