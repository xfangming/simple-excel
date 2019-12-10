package com.tobiasy.simple.function;

import com.tobiasy.simple.constants.DateConst;
import com.tobiasy.simple.constants.ExcelConstants;
import com.tobiasy.simple.excel.ExcelAnnotation;
import com.tobiasy.simple.utils.ClassParser;
import com.tobiasy.simple.utils.DateUtils;
import com.tobiasy.simple.utils.EnumUtils;
import com.tobiasy.simple.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * @author tobiasy
 * @date 2019/7/19
 */
public class ExcelFunction {
    public static Function<String, Integer> TO_INTEGER_ = ExcelFunction::parseInt;
    public static Function<String, Object> TO_INTEGER = ExcelFunction::parseInt;
    public static Function<String, String> TO_STRING_ = s -> s;
    public static Function<String, Object> TO_STRING = s -> s;
    public static Function<String, Object> TO_DATE;
    public static Function<String, Object> TO_SIMPLE_DATE;
    public static Function<String, Object> TO_DEFAULT_DATE;
    public static Function<String, BigDecimal> TO_BIGDECIMAL_ = BigDecimal::new;
    public static Function<String, Object> TO_BIGDECIMAL = BigDecimal::new;
    public static Function<String, Double> TO_DOUBLE_ = Double::new;
    public static Function<String, Object> TO_DOUBLE = Double::new;
    public static Function<String, Float> TO_FLOAT_ = Float::new;
    public static Function<String, Object> TO_FLOAT = Float::new;
    public static Function<String, Long> TO_LONG_ = Long::new;
    public static Function<String, Object> TO_LONG = Long::new;
    public static Function<String, Boolean> TO_BOOLEAN_ = Boolean::new;
    public static Function<String, Object> TO_BOOLEAN = Boolean::new;
    public static BiFunction<String, Class, Enum> TO_ENUM_NAME;
    public static BiFunction<String, Class, Enum> TO_ENUM_VALUE;
    public static BiFunction<String, String, Date> TO_DATE_FORMAT;
    public static String DATE_DEFAULT_PATTERN = DateConst.SIMPLE_DATE_PATTERN;

    static {
        TO_DATE = (s) -> TO_DATE_FORMAT.apply(s, DATE_DEFAULT_PATTERN);
        TO_DEFAULT_DATE = (s) -> TO_DATE_FORMAT.apply(s, DATE_DEFAULT_PATTERN);
        TO_SIMPLE_DATE = (s) -> TO_DATE_FORMAT.apply(s, DATE_DEFAULT_PATTERN);
        TO_ENUM_NAME = (s, clazz) -> EnumUtils.getEnumByAttr(clazz, "name", s);
        TO_ENUM_VALUE = (s, clazz) -> EnumUtils.getEnumByAttr(clazz, "value", s);
        TO_DATE_FORMAT = DateUtils::parse;
    }

    public static ExcelFunction getExcelFunction(Class<?> clazz) {
        return ExcelAnnotation.getTargetFunction(clazz);
    }

    public Function<String, Date> format(String pattern) {
        return (s) -> TO_DATE_FORMAT.apply(s, pattern);
    }

    public static Integer parseInt(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return ClassParser.parseInt(str);
    }

    /**
     * 导出枚举自定义函数，获取枚举中的value值
     *
     * @param clazz
     * @return
     */
    public static Function<String, Enum> getEnumName(Class clazz) {
        return (s) -> TO_ENUM_NAME.apply(s, clazz);
    }

    public static Function<String, Enum> getEnumValue(Class clazz) {
        return (s) -> TO_ENUM_VALUE.apply(s, clazz);
    }

    /**
     * 设置当前对象导出时间格式
     *
     * @return
     */
    public String getDefaultPattern() {
        return DATE_DEFAULT_PATTERN;
    }

    /**
     * 设置当前对象导出是否需要加载枚举类型，默认：不需要
     *
     * @return
     */
    public boolean selectEnum() {
        return false;
    }

    /**
     * 枚举类型显示字段名
     *
     * @return
     */
    public String enumTypeName() {
        return ExcelConstants.EXCEL_ENUM_VALUE;
    }
}
