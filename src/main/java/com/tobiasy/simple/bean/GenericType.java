package com.tobiasy.simple.bean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author tobiasy
 * @date 2019/11/19
 * @param <T>
 */
public class GenericType<T> {

    private Type type;
    private Class<T> classType;

    @SuppressWarnings("unchecked")
    public GenericType() {
        Type superClass = getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        if (this.type instanceof ParameterizedType) {
            this.classType = (Class<T>) ((ParameterizedType) this.type).getRawType();
        } else {
            this.classType = (Class<T>) this.type;
        }
    }

    public Type getType() {
        return type;
    }

    public Class<T> getClassType() {
        return classType;
    }

    public static void main(String[] args) {
        GenericType<List<String>> defaultTargetType = new GenericType<List<String>>() {};
        Class<List<String>> classType = defaultTargetType.getClassType();
        System.out.println(classType);
    }
}