package com.tobiasy.simple.enums;


import com.tobiasy.simple.utils.ClassParser;

import java.util.Date;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * @author tobiasy
 * @date 2018/10/14
 */
public enum ClassEnum {
    /*
     * 类型枚举
     */
    STRING(String.class, String::valueOf, String[]::new),
    BYTE(Byte.class, ClassParser::parseByte, Byte[]::new),
    SHORT(Short.class, ClassParser::parseShort, Short[]::new),
    INT(int.class, ClassParser::parseInt, int[]::new),
    INTEGER(Integer.class, ClassParser::parseInt, Integer[]::new),
    LONG(long.class, ClassParser::parseLong, long[]::new),
    LONG_MAX(Long.class, ClassParser::parseLong, Long[]::new),
    FLOAT(float.class, ClassParser::parseFloat, float[]::new),
    FLOAT_MAX(Float.class, ClassParser::parseFloat, Float[]::new),
    DOUBLE(double.class, ClassParser::parseDouble, double[]::new),
    DOUBLE_MAX(Double.class, ClassParser::parseDouble, Double[]::new),
    BOOLEAN(boolean.class, ClassParser::parseBoolean, boolean[]::new),
    BOOLEAN_MAX(Boolean.class, ClassParser::parseBoolean, Boolean[]::new),
    DATE(Date.class, ClassParser::parseDate, Date[]::new),
    CHAR(char.class, ClassParser::parseChar, char[]::new),
    CHARACTER(Character.class, ClassParser::parseChar, Character[]::new),
    OBJECT(Object.class, (p) -> p, Object[]::new);

    private Class<?> clazz;
    private Function function;
    private IntFunction intFunction;

    ClassEnum(Class<?> clazz, Function function, IntFunction intFunction) {
        this.clazz = clazz;
        this.function = function;
        this.intFunction = intFunction;
    }

    /**
     * 初始化
     *
     * @param clazz 类型大写
     * @return
     */
    public static ClassEnum getInstance(Class<?> clazz) {
        for (ClassEnum classEnum : ClassEnum.values()) {
            if (classEnum.getClazz() == clazz) {
                return classEnum;
            }
        }
        return ClassEnum.OBJECT;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public <T, K> Function<T, K> getFunction() {
        return (Function<T, K>) function;
    }

    public void setFunction(Function<?, ?> function) {
        this.function = function;
    }

    public <K> IntFunction<K[]> getIntFunction() {
        return (IntFunction<K[]>) intFunction;
    }

    public void setIntFunction(IntFunction intFunction) {
        this.intFunction = intFunction;
    }}
