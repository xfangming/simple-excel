package com.tobiasy.simple.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author tobiasy
 * @date 2018/12/14
 */
public class CellStyleHandler {
    public static Boolean addStyleFromFile(File file,
                                           int oldRowIndex, int oldCellIndex,
                                           File target,
                                           int newRowIndex, int newCellIndex
    ) {
        return addStyleFromFile(file, 0, oldRowIndex, oldCellIndex,
                target, 0, newRowIndex, newCellIndex, false);
    }

    public static Boolean addStyleFromFile(File file,
                                           int oldSheetIndex, int oldRowIndex, int oldCellIndex,
                                           File target,
                                           int newSheetIndex, int newRowIndex, int newCellIndex
    ) {
        return addStyleFromFile(file, oldSheetIndex, oldRowIndex, oldCellIndex,
                target, newSheetIndex, newRowIndex, newCellIndex, false);
    }

    public static Boolean addStyleFromFile(File file,
                                           int oldSheetIndex, int oldRowIndex, int oldCellIndex,
                                           File target,
                                           int newSheetIndex, int newRowIndex, int newCellIndex,
                                           boolean overrideValue
    ) {
        try {
            Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
            Sheet sheet = workbook.getSheetAt(oldSheetIndex);
            int sheetWidth = sheet.getColumnWidth(oldCellIndex);
            Row row = sheet.getRow(oldRowIndex);
            Cell cell = row.getCell(oldCellIndex);
            CellStyle cellStyle = cell.getCellStyle();
            HSSFWorkbook newWorkbook = new HSSFWorkbook(new FileInputStream(target));
            Sheet newSheet = newWorkbook.getSheetAt(newSheetIndex);
            newSheet.setColumnWidth(newCellIndex, sheetWidth);
            Row newRow = newSheet.getRow(newRowIndex);
            if (newRow == null) {
                newRow = newSheet.createRow(newRowIndex);
            }
            newRow.setHeight(row.getHeight());
            Cell newCell = newRow.getCell(newCellIndex);
            if (newCell == null) {
                newCell = newRow.createCell(newCellIndex);
            }
            if (overrideValue) {
                newCell.setCellValue(cell.getStringCellValue());
            }
            CellStyle newCellStyle = newWorkbook.createCellStyle();
            newCellStyle.cloneStyleFrom(cellStyle);
            newCell.setCellStyle(newCellStyle);
            return ExcelUtils.createExcel(newWorkbook, target);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean addAllStyle(File file, File target) {
        return addAllStyle(file, 0, target, 0);
    }

    public static Boolean addAllStyle(File file,
                                      int oldSheetIndex,
                                      File target,
                                      int newSheetIndex
    ) {
        try {
            Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
            Sheet sheet = workbook.getSheetAt(oldSheetIndex);
            HSSFWorkbook newWorkbook = new HSSFWorkbook(new FileInputStream(target));
            Sheet newSheet = newWorkbook.getSheetAt(newSheetIndex);
            boolean setWidth = true;
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Row newRow = newSheet.getRow(i);
                if (newRow == null) {
                    newRow = newSheet.createRow(i);
                }
                newRow.setHeight(row.getHeight());
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    if (setWidth) {
                        int sheetWidth = sheet.getColumnWidth(j);
                        newSheet.setColumnWidth(j, sheetWidth);
                    }
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        continue;
                    }
                    CellStyle cellStyle = cell.getCellStyle();
                    Cell newCell = newRow.getCell(j);
                    if (newCell == null) {
                        newCell = newRow.createCell(j);
                    }
                    CellStyle newCellStyle = newWorkbook.createCellStyle();
                    newCellStyle.cloneStyleFrom(cellStyle);
                    newCell.setCellStyle(newCellStyle);
                }
                setWidth = false;
            }
            return ExcelUtils.createExcel(newWorkbook, target);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
} 