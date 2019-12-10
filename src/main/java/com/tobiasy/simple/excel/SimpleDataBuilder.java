package com.tobiasy.simple.excel;

import com.tobiasy.simple.enums.ExcelTypeEnum;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tobiasy
 * @date 2019/10/22
 */
public class SimpleDataBuilder {
    /**
     * 获取excel文件中的指定数据
     *
     * @param file     文件
     * @param sheetAt  所在sheet
     * @param firstRow 起始行
     * @param cellNum  所在列
     * @return
     */
    public static List<String> getExcelData(File file, Integer sheetAt, Integer firstRow, Integer lastRow, Integer cellNum) {
        ExcelTypeEnum excelType = ExcelTypeUtils.getExcelType(file.getName());
        return getExcelData(FileUtils.getInputStream(file), excelType, sheetAt, firstRow, lastRow, cellNum);
    }

    public static List<String> getExcelData(File file, Integer sheetAt, Integer firstRow, Integer cellNum) {
        return getExcelData(file, sheetAt, firstRow, null, cellNum);
    }

    /**
     * @param input         文件输入流
     * @param excelTypeEnum excel类型
     * @param sheetAt       所在sheet
     * @param firstRow      起始行
     * @param lastRow       终止行
     * @param cellNum       所属列
     * @return
     */
    public static List<String> getExcelData(InputStream input, ExcelTypeEnum excelTypeEnum,
                                            Integer sheetAt, Integer firstRow, Integer lastRow, Integer cellNum) {
        Sheet sheet = ExcelTypeUtils.getSheet(input, excelTypeEnum, sheetAt);
        int lastRowNum;
        if (lastRow == null) {
            lastRowNum = sheet.getLastRowNum();
        } else {
            lastRowNum = lastRow;
        }
        List<String> list = new ArrayList<>();
        for (int i = firstRow; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Cell cell = row.getCell(cellNum);
            String cellValue = CellUtils.getCellValue(cell);
            list.add(cellValue);
        }
        return list;
    }
}
