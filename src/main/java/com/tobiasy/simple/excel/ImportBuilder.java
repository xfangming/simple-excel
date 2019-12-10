package com.tobiasy.simple.excel;

import com.tobiasy.simple.anno.ImportUseAnnotation;
import com.tobiasy.simple.api.Position;
import com.tobiasy.simple.bean.OrderField;
import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.exception.OperationException;
import com.tobiasy.simple.utils.ArrayUtils;
import com.tobiasy.simple.utils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Excel导入核心处理类
 *
 * @author tobiasy
 * @date 2019/7/16
 */
public class ImportBuilder {

    private static Logger log = Logger.getLogger("ImportBuilder");

    public static <T> List<T> getList(InputStream input, Supplier<T> newInstance, ExcelTypeEnum excelTypeEnum) {
        Class<?> clazz = newInstance.get().getClass();
        List<OrderField> fields = ExcelAnnotation.getFieldOrderFunction(clazz);
        return getList(input, fields, newInstance, excelTypeEnum);
    }

    /**
     * 自然映射
     *
     * @return
     */
    public static <T> List<T> getList(File file, Integer firstRow,
                                      String[] attrs, Function<String, Object>[] functions,
                                      Supplier<T> newInstance) {
        ExcelTypeEnum excelType = ExcelTypeUtils.getExcelType(file.getName());
        return getList(file, 0, firstRow, attrs, functions, newInstance, excelType);
    }

    public static Position getPosition(ImportUseAnnotation importType) {
        int sheetAt = 0;
        int firstRow = 0;
        if (importType != null) {
            sheetAt = importType.sheetAt();
            firstRow = importType.firstRow();
        } else {
            log.info("当未指定导出函数时可以使用@ImportUseAnnotation设置参数[sheetAt, firstRow]，缺省值均为0");
        }
        return Position.of(sheetAt, firstRow, 0);
    }

    public static <T> List<T> getList(InputStream input,
                                      List<OrderField> fields,
                                      Supplier<T> newInstance, ExcelTypeEnum excelTypeEnum) {
        ImportUseAnnotation importType = ExcelAnnotation.getImportType(newInstance.get().getClass());
        Position position = getPosition(importType);
        List<Map<Integer, String>> list = getData(input, excelTypeEnum, position.getSheetAt(), position.getStartRow());
        return parse(list, fields, newInstance, importType);
    }

    public static <T> List<T> getList(File input, Integer sheetAt, Integer firstRow,
                                      String[] attrs, Function<String, Object>[] functions,
                                      Supplier<T> newInstance, ExcelTypeEnum excelType) {
        return getList(FileUtils.getInputStream(input), sheetAt, firstRow, attrs, functions, newInstance, excelType);
    }

    /**
     * 字符函数方式
     *
     * @param input         输入流
     * @param sheetAt       当前sheet
     * @param firstRow      首行
     * @param attrs         操作属性
     * @param functions     转换函数
     * @param newInstance   初始化函数
     * @param excelTypeEnum excel文件类型
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(InputStream input, Integer sheetAt, Integer firstRow,
                                      String[] attrs, Function<String, Object>[] functions,
                                      Supplier<T> newInstance, ExcelTypeEnum excelTypeEnum) {
        List<Map<Integer, String>> list = getData(input, excelTypeEnum, sheetAt, firstRow);
        return parse(list, attrs, functions, newInstance);
    }

    /**
     * 注解函数方式
     * 通过setter函数得到数据
     *
     * @param input         输入流
     * @param sheetAt       当前sheet
     * @param firstRow      首行
     * @param setters       setter函数
     * @param newInstance   对象初始化函数
     * @param excelTypeEnum excel文件类型
     * @param <T>           对象枚举
     * @return
     */
    public static <T> List<T> getList(InputStream input, Integer sheetAt, Integer firstRow,
                                      BiConsumer<T, String>[] setters,
                                      Supplier<T> newInstance, ExcelTypeEnum excelTypeEnum) {
        List<Map<Integer, String>> list = getData(input, excelTypeEnum, sheetAt, firstRow);
        return parse(list, setters, newInstance);
    }

    /*
     ********************************** 解析数据 **********************************
     */

    public static <T> List<T> parse(List<Map<Integer, String>> list,
                                    BiConsumer<T, String>[] setters,
                                    Supplier<T> newInstance) {
        List<T> result = new ArrayList<>();
        for (Map<Integer, String> dataMap : list) {
            T t = newInstance.get();
            for (int i = 0; i < setters.length; i++) {
                String value = dataMap.get(i);
                if (value != null) {
                    setters[i].accept(t, value);
                }
            }
            result.add(t);
        }
        return result;
    }

    public static <T> List<T> parse(List<Map<Integer, String>> list,
                                    String[] attrs, Function<String, Object>[] functions,
                                    Supplier<T> newInstance) {
        List<T> result = new ArrayList<>();
        for (Map<Integer, String> dataMap : list) {
            T t = newInstance.get();
            for (int i = 0; i < attrs.length; i++) {
                String value = dataMap.get(i);
                if (value != null) {
                    Object val = ArrayUtils.getNotNullArray(functions, i).apply(value);
                    BeanUtils.setProperty(t, attrs[i], val);
                }
            }
            result.add(t);
        }
        return result;
    }

    public static <T> List<T> parse(List<Map<Integer, String>> list, List<OrderField> orderFields,
                                    Supplier<T> newInstance, ImportUseAnnotation importType) {
        List<T> result = new ArrayList<>();
        for (Map<Integer, String> dataMap : list) {
            T t = newInstance.get();
            for (int i = 0; i < orderFields.size(); i++) {
                OrderField orderField = orderFields.get(i);
                Integer index = i;
                if (importType != null) {
                    index = orderField.getIndex();
                }
                String value = dataMap.get(index);
                if (value != null) {
                    Function<String, Object> function = orderField.getFunction();
                    Object val;
                    try {
                        val = function.apply(value);
                    } catch (Exception e) {
                        throw new OperationException(e.getMessage());
                    }
                    if (val != null) {
                        BeanUtils.setProperty(t, orderField.getField(), val);
                    }
                }
            }
            result.add(t);
        }
        return result;
    }

    /*
     ********************************** 获取数据 **********************************
     */

    public static List<Map<Integer, String>> getData(File file, Integer firstRow) {
        return getData(file, 0, firstRow);
    }

    public static List<Map<Integer, String>> getData(File file, Integer sheetAt, Integer firstRow) {
        ExcelTypeEnum excelType = ExcelTypeUtils.getExcelType(file.getName());
        return getData(FileUtils.getInputStream(file), excelType, sheetAt, firstRow);
    }

    public static List<Map<Integer, String>> getData(InputStream input, ExcelTypeEnum excelTypeEnum, Integer sheetAt, Integer firstRow) {
        Sheet sheet = ExcelTypeUtils.getSheet(input, excelTypeEnum, sheetAt);
        int lastRowNum = sheet.getLastRowNum();
        List<Map<Integer, String>> list = new ArrayList<>();
        for (int i = firstRow; i <= lastRowNum; i++) {
            Map<Integer, String> map = new HashMap<>(16);
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            short firstCellNum = row.getFirstCellNum();
            short lastCellNum = row.getLastCellNum();
            for (int j = firstCellNum; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                String cellValue = CellUtils.getCellValue(cell);
                map.put(j, cellValue);

            }
            list.add(map);
        }
        return list;
    }
}
