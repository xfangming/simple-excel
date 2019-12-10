package com.tobiasy.simple.excel;

import com.tobiasy.simple.bean.OrderField;
import com.tobiasy.simple.utils.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author tobiasy
 * @date 2018/12/13
 */
public class RowUtils {

    private static final Integer DEFAULT_FIRST_COL = 0;

    /**
     * @param row  excel行对象
     * @param attr 每一列中所需要获取的属性名称
     * @param t    获取数据的对象
     * @param <T>  t
     */
    public static <T> void doRow(Row row, T t, String[] attr) {
        doRow(row, t, attr, 0, null);
    }


    /**
     * @param row       excel行对象
     * @param attr      每一列中所需要获取的属性名称
     * @param t         获取数据的对象
     * @param firstCol  起始列
     * @param cellStyle 单元格样式
     * @param <T>       t
     */
    public static <T> void doRow(Row row, T t, String[] attr, Integer firstCol, CellStyle cellStyle) {
        if (firstCol == null) {
            firstCol = DEFAULT_FIRST_COL;
        }
        for (int j = 0; j < attr.length; j++) {
            String field = attr[j];
            Cell cell = row.getCell(j + firstCol);
            if (cell == null) {
                cell = row.createCell(j + firstCol);
            }
            CellUtils.doCell(cell, t, new OrderField(j, field), cellStyle);
        }
    }

    public static <T, R> void doRow(Row row, T t, Function<T, R>[] functions, Integer firstCol, CellStyle cellStyle) {
        if (firstCol == null) {
            firstCol = DEFAULT_FIRST_COL;
        }
        for (int j = 0; j < functions.length; j++) {
            Function<T, R> function = functions[j];
            Cell cell = row.getCell(j + firstCol);
            if (cell == null) {
                cell = row.createCell(j + firstCol);
            }
            R value = function.apply(t);
            CellUtils.doCell(cell, value);
            cell.setCellStyle(cellStyle);
        }
    }

    private static Map<Integer, String> formulaStrMap = new HashMap<>();

    /**
     * 对象中的数据将会把属性（Map.key）放在对应的序列（Map.value）中
     *
     * @param row         excel行
     * @param t           对象
     * @param orderFields 属性序列集合
     * @param cellStyle   表格样式
     * @param <T>         t
     */
    public static <T> void doRow(Row row, T t, List<OrderField> orderFields,
                                 CellStyle cellStyle) {
        for (OrderField orderField : orderFields) {
            Cell cell = CellUtils.getCurrCell(row, orderField.getIndex());
            if (orderField.getField() != null) {
                CellUtils.doCell(cell, t, orderField, cellStyle);
            } else if (orderField.getFormula() != null) {
                Integer index = row.getRowNum() + 1;
                String formulaStr = formulaStrMap.get(orderField.getIndex());
                if (formulaStr == null) {
                    formulaStr = StringUtils.replace(orderField.getFormula());
                    formulaStrMap.put(orderField.getIndex(), formulaStr);
                }
                String formula = String.format(formulaStr, index);
                CellUtils.addCellFormula(cell, formula);
                cell.setCellStyle(cellStyle);
            }
            Integer columnSize = orderField.getColumnSize();
            if (columnSize > 1) {
                CellRangeAddress cellRange = new CellRangeAddress(
                        row.getRowNum(), row.getRowNum(),
                        orderField.getIndex(), orderField.getIndex() + columnSize - 1);
                row.getSheet().addMergedRegion(cellRange);
            }
        }
    }
} 