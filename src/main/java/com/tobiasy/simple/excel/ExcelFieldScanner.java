package com.tobiasy.simple.excel;

import com.tobiasy.simple.anno.ExcelField;
import com.tobiasy.simple.bean.OrderField;
import com.tobiasy.simple.utils.Generate;
import com.tobiasy.simple.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author tobiasy
 * @date 2019/7/18
 */
public class ExcelFieldScanner {
    /**
     * 处理扫描到的数据 - 单独获取属性集合
     *
     * @param excelFieldList 属性集合
     * @return List<String>
     */
    public static String[] getOrderFields(List<OrderField> excelFieldList) {
        return excelFieldList.stream().map(OrderField::getField).toArray(String[]::new);
    }

    /**
     * 获取Class中{@link ExcelField }注解标注的所有属性、函数集合
     *
     * @param c 对象Class
     * @return List<OrderField>
     */
    public static List<OrderField> getExcelFieldList(Class c) {
        List<Field> globalFields = ReflectUtils.getGlobalFields(c);
        List<OrderField> orderFields = new LinkedList<>();
        for (Field globalField : globalFields) {
            ExcelField annotation = globalField.getAnnotation(ExcelField.class);
            if (annotation != null) {
                int index = annotation.index();
                String name = annotation.name();
                OrderField orderField = new OrderField(index, globalField.getName());
                orderField.setName(name);
                orderField.setFieldGetter(Generate.toGetter(globalField.getName()));
                orderField.setColumnSize(annotation.columnSize());
                orderField.setFunctionName(annotation.function());
                orderField.setDateFormat(annotation.dateFormat());
                orderFields.add(orderField);
            }
        }
        Collections.sort(orderFields);
        return orderFields;
    }
}
