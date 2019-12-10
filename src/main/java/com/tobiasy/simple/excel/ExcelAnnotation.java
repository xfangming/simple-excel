package com.tobiasy.simple.excel;

import com.tobiasy.simple.anno.ExcelField;
import com.tobiasy.simple.anno.ExportUseAnnotation;
import com.tobiasy.simple.anno.FunctionTarget;
import com.tobiasy.simple.anno.ImportUseAnnotation;
import com.tobiasy.simple.bean.FieldFunction;
import com.tobiasy.simple.bean.OrderField;
import com.tobiasy.simple.exception.OperationException;
import com.tobiasy.simple.function.ExcelFunction;
import com.tobiasy.simple.utils.BeanUtils;
import com.tobiasy.simple.utils.ClassHelper;
import com.tobiasy.simple.utils.ReflectUtils;
import com.tobiasy.simple.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * @author tobiasy
 * @date 2019/7/18
 * @see ExcelFieldScanner
 */
public class ExcelAnnotation {

    /**
     * 获取Class对象中{@link ExcelField }注解标注的属性集合，排序
     *
     * @param clazz 操作对象Class
     * @return
     */
    public static String[] getOrderFields(Class clazz) {
        List<OrderField> excelFieldList = ExcelFieldScanner.getExcelFieldList(clazz);
        String[] excelField = ExcelFieldScanner.getOrderFields(excelFieldList);
        if (excelField.length == 0) {
            throw new OperationException("%s对象中没有扫描到需要导出的属性！请使用%s进行标注。", clazz, ExcelField.class);
        }
        return excelField;
    }

    public static <T> String[] getOrderFields(List<T> list) {
        Class<?> clazz = ClassHelper.getClass(list);
        return getOrderFields(clazz);
    }

    public static Function<String, Object> getFunction(OrderField orderField, FunctionTarget functionTarget) {
        String function = orderField.getFunctionName();
        Class<?> clazz;
        if (functionTarget == null) {
            clazz = ExcelFunction.class;
        } else {
            clazz = functionTarget.value();
        }
        if (StringUtils.notEmpty(orderField.getDateFormat())) {
            String dateFormat = orderField.getDateFormat();
            if (StringUtils.isEmpty(dateFormat)) {
                dateFormat = ExcelFunction.DATE_DEFAULT_PATTERN;
            }
            String datePattern = dateFormat;
            return (s) -> ExcelFunction.TO_DATE_FORMAT.apply(s, datePattern);
        } else {
            Field field = ReflectUtils.findGlobalField(clazz, function);
            if (field == null) {
                throw new OperationException("%s中没有找到转换函数%s", clazz, function);
            }
            return getFieldValue(field, clazz);
        }
    }

    private static Function<String, Object> getFieldValue(Field field, Class<?> clazz) {
        try {
            Object o = BeanUtils.newInstance(clazz);
            return (Function) field.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ExcelFunction.TO_STRING;
    }

    /**
     * 获取Class中需要进行excel导入操作的属性及其转换函数
     *
     * @param clazz excel实际操作对象
     * @return
     */
    public static List<OrderField> getFieldOrderFunction(Class clazz) {
        FunctionTarget functionTarget = getFunctionTarget(clazz);
        List<OrderField> excelFields = ExcelFieldScanner.getExcelFieldList(clazz);
        for (OrderField excelField : excelFields) {
            Function<String, Object> functions = getFunction(excelField, functionTarget);
            excelField.setFunction(functions);
        }
        return excelFields;
    }

    @Deprecated
    public static FieldFunction getFieldFunction(Class<?> clazz) {
        FieldFunction fieldFunction = new FieldFunction();
        List<OrderField> excelFields = getFieldOrderFunction(clazz);
        String[] attrs = ExcelFieldScanner.getOrderFields(excelFields);
        fieldFunction.setFields(attrs);
        IntFunction<Function<String, Object>[]> intConsumer = Function[]::new;
        Function<String, Object>[] functions = excelFields.stream().map(OrderField::getFunction).toArray(intConsumer);
        fieldFunction.setFunctions(functions);
        return fieldFunction;
    }

    public static FunctionTarget getFunctionTarget(Class clazz) {
        return (FunctionTarget) clazz.getAnnotation(FunctionTarget.class);
    }

    public static <T> Class<? extends ExcelFunction> getTargetValue(Class<T> clazz) {
        FunctionTarget functionTarget = getFunctionTarget(clazz);
        if (functionTarget == null) {
            return ExcelFunction.class;
        }
        return functionTarget.value();
    }

    public static <T> ExcelFunction getTargetFunction(Class<T> clazz) {
        Class<? extends ExcelFunction> classValue = getTargetValue(clazz);
        ExcelFunction excelFunction = BeanUtils.newInstance(classValue);
        if (excelFunction != null) {
            return excelFunction;
        } else {
            throw new OperationException("'%s'不能初始化！", classValue);
        }
    }

    public static <T> boolean getTargetSelectEnum(Class<T> clazz) {
        return getTargetFunction(clazz).selectEnum();
    }

    public static ExportUseAnnotation getExportUseAnnotation(Class clazz) {
        return getAnnotation(clazz, ExportUseAnnotation.class);
    }

    public static ImportUseAnnotation getImportType(Class clazz) {
        return getAnnotation(clazz, ImportUseAnnotation.class);
    }

    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotation) {
        if (clazz == null) {
            return null;
        }
        return clazz.getAnnotation(annotation);
    }

    public static <T> ExcelFunction newInstance(Class<T> clazz) {
        Class<? extends ExcelFunction> classValue = getTargetValue(clazz);
        return BeanUtils.newInstance(classValue);
    }

    public static <T> String getDatePattern(Class<T> clazz) {
        return newInstance(clazz).getDefaultPattern();
    }

    public static <T> String enumTypeName(Class<T> clazz) {
        return newInstance(clazz).enumTypeName();
    }
}
