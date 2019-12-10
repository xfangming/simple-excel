package com.tobiasy.simple.excel;

import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.exception.OperationException;
import com.tobiasy.simple.function.ExcelFunction;
import com.tobiasy.simple.utils.ArrayUtils;
import com.tobiasy.simple.utils.ClassParser;
import com.tobiasy.simple.utils.DateUtils;
import com.tobiasy.simple.utils.EnumUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * @author tobiasy
 * @date 2019/10/29
 */
public class ExcelHelper extends ExcelFunction {
    public static final Function<File, InputStream> TO_INPUT_STREAM = FileUtils::getInputStream;
    private Class<?> clazz;
    private ExcelFunction excelFunction;
    private String pattern;
    public Function<String, Object> toDate;

    private ExcelHelper() {
    }

    public ExcelHelper(Class<?> clazz) {
        this.clazz = clazz;
        this.excelFunction = getExcelFunction(clazz);
        this.pattern = excelFunction.getDefaultPattern();
        toDate = (s) -> TO_DATE_FORMAT.apply(s, pattern);
    }

    public static ExcelHelper getInstance(Class<?> clazz) {
        return new ExcelHelper(clazz);
    }

    public static ExcelFunction getExcelFunction(Class<?> clazz) {
        if (clazz == null) {
            throw new OperationException("当前对象类型不能为空！");
        }
        return ExcelFunction.getExcelFunction(clazz);
    }

    public static String getDatePattern(Class<?> clazz) {
        ExcelFunction excelFunction = getExcelFunction(clazz);
        return excelFunction.getDefaultPattern();
    }

    public static <T> T parse(Object o, Class<T> clazz) {
        return ClassParser.parse(o, clazz);
    }

    public Date parseDate(Object o) {
        return DateUtils.parse(o, pattern);
    }

    public static Date parseDate(Object o, String pattern) {
        return DateUtils.parse(o, pattern);
    }

    public static Function<String, Object> toEnumName(Class enumClass) {
        return (s) -> TO_ENUM_NAME.apply(s, enumClass);
    }

    public static Function<String, Object> toEnumValue(Class enumClass) {
        return (s) -> TO_ENUM_VALUE.apply(s, enumClass);
    }

    public static Function<String, Object> toEnum(String fieldName, Class enumClass) {
        BiFunction<String, Class, Enum> function = (s, clazz) -> EnumUtils.getEnumByAttr(clazz, fieldName, s);
        return (s) -> function.apply(s, enumClass);
    }

    public static InputStream toInputStream(File file) {
        return FileUtils.getInputStream(file);
    }

    public static ExcelTypeEnum getExcelType(File file) {
        return ExcelTypeUtils.getExcelType(file.getName());
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @SafeVarargs
    public static <T> T[] asArray(T... ts) {
        return ts;
    }

    @SafeVarargs
    public static <T, R> Function<T, R>[] asArray(Function<T, R>... functions) {
        return functions;
    }

    @SafeVarargs
    public static <T, R> Function<T, Object>[] asObjectArray(Function<T, R>... functions) {
        IntFunction<Function<T, Object>[]> intFunction = Function[]::new;
        Function<T, Object>[] result = ArrayUtils.getArray(intFunction);
        Function<T, R>[] array = ArrayUtils.asArray(functions);
        for (int i = 0; i < array.length; i++) {
            Function<T, R> function = array[i];
            result[i] = toObjectArray(function);
        }
        return result;
    }

    public static <T, R> Function<T, Object> toObjectArray(Function<T, R> function) {
        return (t) -> {
            R apply = function.apply(t);
            return (Object) apply;
        };
    }

    public static <T, R> R toAnother(T t, Function<T, R> function) {
        return function.apply(t);
    }
}
