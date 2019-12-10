package com.tobiasy.simple.excel;

import com.tobiasy.simple.bean.OrderField;
import com.tobiasy.simple.constants.DateConst;
import com.tobiasy.simple.constants.ExcelConstants;
import com.tobiasy.simple.enums.CellTypeEnum;
import com.tobiasy.simple.exception.OperationException;
import com.tobiasy.simple.function.ExcelFunction;
import com.tobiasy.simple.utils.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tobiasy
 * @date 2018/10/17
 */
public class CellUtils {
    /**
     * 根据下标获取row中的单元格
     *
     * @param row
     * @param index
     * @return
     */
    public static Cell getCurrCell(Row row, Integer index) {
        Cell cell = row.getCell(index);
        if (cell != null) {
            return cell;
        }
        return row.createCell(index);
    }

    /**
     * 获取单元格的字符串值
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        CellType typeEnum = cell.getCellTypeEnum();
        switch (typeEnum) {
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case NUMERIC:
                cell.setCellType(CellType.STRING);
                return cell.getStringCellValue();
            case STRING:
                return cell.getStringCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            default:
                return cell.getStringCellValue();
        }
    }

    /**
     * 单元格属性值设置
     *
     * @param cell  单元格
     * @param value 写入值
     */
    public static Cell doCell(Cell cell, Object value) {
        if (cell == null || value == null) {
            return null;
        }
        Class<?> fieldType = value.getClass();
        CellTypeEnum typeEnum = CellTypeEnum.getInstance(fieldType);
        if (typeEnum == null) {
            throw new OperationException("当前类型不支持导出！");
        }
        switch (typeEnum) {
            case STRING:
                cell.setCellValue(String.valueOf(value));
                cell.setCellType(CellType.STRING);
                break;
            case NUMERIC:
                int intValue = Integer.parseInt(String.valueOf(value));
                cell.setCellValue(intValue);
                cell.setCellType(CellType.NUMERIC);
                break;
            case DOUBLE:
                BigDecimal doub = new BigDecimal(String.valueOf(value));
                cell.setCellValue(doub.doubleValue());
                cell.setCellType(CellType.NUMERIC);
                break;
            case DATE:
                String date = DateUtils.format(value, DateConst.SIMPLE_DATE_PATTERN);
                cell.setCellValue(date);
                cell.setCellType(CellType.STRING);
                break;
            case BOOLEAN:
                boolean flag;
                flag = Boolean.parseBoolean(String.valueOf(value));
                cell.setCellValue(flag);
                cell.setCellType(CellType.BOOLEAN);
                break;
            case ENUM:
                Enum e = (Enum) value;
                cell.setCellValue(e.name());
                cell.setCellType(CellType.STRING);
                break;
            default:
                cell.setCellValue(value != null ? value.toString() : null);
                cell.setCellType(CellType.STRING);
        }
        return cell;
    }

    public static <T> void doCell(Cell cell, T t, OrderField orderField) {
        doCell(cell, t, orderField, null);
    }

    private static Map<String, Field> fieldMap = new HashMap<>();

    public static <T> void doCell(Cell cell, T t, OrderField orderField, CellStyle cellStyle) {
        String fieldName = orderField.getField();
        Field typeField = fieldMap.get(fieldName);
        if (typeField == null) {
            typeField = ReflectUtils.findGlobalField(t.getClass(), fieldName);
            if (typeField == null) {
                throw new RuntimeException("没有找到该属性：" + fieldName);
            }
            fieldMap.put(fieldName, typeField);
        }
        Class fieldType = typeField.getType();
        Object value;
        try {
            value = ReflectUtils.invoke(t, orderField.getFieldGetter());
            CellTypeEnum typeEnum = CellTypeEnum.getInstance(fieldType);
            if (typeEnum == null) {
                throw new OperationException("当前类型不支持导出！");
            }
            switch (typeEnum) {
                case STRING:
                    cell.setCellValue(value != null ? value.toString() : null);
                    cell.setCellType(CellType.STRING);
                    break;
                case NUMERIC:
                    int intValue;
                    if (value != null) {
                        intValue = Integer.parseInt(String.valueOf(value));
                        cell.setCellValue(intValue);
                    }
                    break;
                case DOUBLE:
                    BigDecimal doub;
                    if (value != null) {
                        doub = new BigDecimal(String.valueOf(value));
                        cell.setCellValue(doub.doubleValue());
                    }
                    break;
                case DATE:
                    String dateFormat;
                    if (value != null) {
                        if (StringUtils.isEmpty(orderField.getDateFormat())) {
                            dateFormat = ExcelFunction.DATE_DEFAULT_PATTERN;
                        } else {
                            dateFormat = orderField.getDateFormat();
                        }
                        cell.setCellValue(DateUtils.format(value, dateFormat));
                        cell.setCellType(CellType.STRING);
                    }
                    break;
                case BOOLEAN:
                    boolean flag;
                    if (value != null) {
                        flag = Boolean.parseBoolean(String.valueOf(value));
                        cell.setCellValue(flag);
                        cell.setCellType(CellType.BOOLEAN);
                    }
                    break;
                case ENUM:
                    enumType(cell, fieldType, value, t.getClass());
                    break;
                default:
                    cell.setCellValue(value != null ? value.toString() : null);
                    cell.setCellType(CellType.STRING);
            }
            if (cellStyle != null) {
                cell.setCellStyle(cellStyle);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private static <T> void enumType(Cell cell, Class fieldType, Object value, Class<T> targetClass) {
        String enumValue;
        boolean targetSelectEnum = ExcelAnnotation.getTargetSelectEnum(targetClass);
        // 判断当前对象导出是否需要加载枚举类型
        if (targetSelectEnum) {
            if (fieldType.isEnum()) {
                String enumTypeName = ExcelAnnotation.enumTypeName(targetClass);
                try {
                    enumValue = EnumUtils.getEnumValue(fieldType, enumTypeName);
                    CellRangeAddressList regions = new CellRangeAddressList(
                            cell.getRowIndex(), cell.getRowIndex(),
                            cell.getColumnIndex(), cell.getColumnIndex());
                    DataValidation dataValidation = ExcelTypeUtils.getDataValidation(
                            cell.getRow().getSheet(),
                            regions,
                            enumValue.split(","));
                    cell.getRow().getSheet().addValidationData(dataValidation);
                } catch (OperationException e) {

                }
            }
        }
        Enum e = (Enum) value;
        Object getValue;
        try {
            getValue = ReflectUtils.invokeSkipError(e, Generate.toGetter(ExcelConstants.EXCEL_ENUM_VALUE));
        } catch (Exception ex) {
            getValue = e.name();
        }
        if (getValue != null) {
            cell.setCellValue(getValue.toString());
            cell.setCellType(CellType.STRING);
        } else {
            String val = value != null ? value.toString() : null;
            cell.setCellValue(val);
            cell.setCellType(CellType.STRING);
        }
    }

    /**
     * 单元格中加入函数
     *
     * @param cell
     * @param formula 函数全称  SUM(A1：A7)
     */
    public static void addCellFormula(Cell cell, String formula, Object... args) {
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula(String.format(formula, args));
    }

    /**
     * 通过函数名在单元格中加入函数
     *
     * @param cell          单元格
     * @param formulaPrefix 函数名称  SUM
     */
    public static void addCellFormulaByPrefix(HSSFCell cell, String formulaPrefix, Integer start, Integer end) {
        String str = CellUtils.getFormula(cell);
        String formula = formulaPrefix + "(" + str + start + ":" + str + end + ")";
        cell.setCellFormula(formula);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula(formula);
    }

    /**
     * 默认设置函数起始位置（第一行开始，最后一行结束）
     *
     * @param cell          单元格
     * @param formulaPrefix 函数前缀
     */
    public static void addDefaultCellFormulaByPrefix(HSSFCell cell, String formulaPrefix) {
        String str = CellUtils.getFormula(cell);
        HSSFSheet sheet = cell.getRow().getSheet();
        String formula = formulaPrefix + "(" + str + (sheet.getFirstRowNum() + 1) + ":" + str + (sheet.getLastRowNum()) + ")";
        cell.setCellFormula(formula);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula(formula);
    }

    /**
     * 获取excel头部的字母标识，添加函数时常用到
     * 例：获取第一列头部的A、第二列的B
     *
     * @param cell
     * @return
     */
    public static String getFormula(HSSFCell cell) {
        return CellReference.convertNumToColString(cell.getColumnIndex());
    }

    public static Integer getIndex(String colString) {
        return CellReference.convertColStringToIndex(colString);
    }
} 