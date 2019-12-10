package com.tobiasy.simple.enums;

/**
 * @author tobiasy
 * @date 2019/7/9
 */
public enum BeanPrefix{
    /**
     * setter方法前缀
     */
    SET("set"),
    /**
     * getter方法前缀
     */
    GET("get"),
    /**
     * 断言方法前缀
     */
    IS("is"),
    /**
     * 是否存在判断方法前缀
     */
    HAS("has");

    private String key;

    BeanPrefix(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}