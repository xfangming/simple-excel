package com.tobiasy.simple.bean;

import com.tobiasy.simple.utils.Generate;

import java.util.function.Function;

/**
 * @author tobiasy
 * @date 2019/7/18
 */
public class OrderField implements Comparable<OrderField> {
    private Integer index;
    private String name;
    private String field;
    private String fieldGetter;
    private Integer columnSize;
    private String functionName;
    private Function<String, Object> function;
    private String formula;
    private String dateFormat;

    public OrderField() {
    }

    public OrderField(Integer index, String field) {
        this.index = index;
        this.field = field;
        this.fieldGetter = Generate.toGetter(field);
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldGetter() {
        return fieldGetter;
    }

    public void setFieldGetter(String fieldGetter) {
        this.fieldGetter = fieldGetter;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Function<String, Object> getFunction() {
        return function;
    }

    public void setFunction(Function<String, Object> function) {
        this.function = function;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public int compareTo(OrderField that) {
        return this.getIndex().compareTo(that.getIndex());
    }
}
