package com.tobiasy.simple.bean;

import java.util.function.Function;

/**
 * @author tobiasy
 * @date 2019/7/19
 */
public class FieldFunction {
    private String[] fields;
    private Function<String, Object>[] functions;

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public Function<String, Object>[] getFunctions() {
        return functions;
    }

    public void setFunctions(Function<String, Object>[] functions) {
        this.functions = functions;
    }
}
