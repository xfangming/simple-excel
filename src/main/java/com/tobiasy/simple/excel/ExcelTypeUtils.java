package com.tobiasy.simple.excel;

import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.exception.OperationException;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom.Attribute;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

/**
 * @author tobiasy
 * @date 2019/7/24
 */
public class ExcelTypeUtils {
    public static Workbook getWorkbook(ExcelTypeEnum typeEnum) {
        Workbook wb;
        if (typeEnum == ExcelTypeEnum.XLS) {
            wb = new HSSFWorkbook();
        } else if (typeEnum == ExcelTypeEnum.XLSX) {
//            wb = new XSSFWorkbook();
            wb = new SXSSFWorkbook();
        } else {
            throw new OperationException("Not support this excelType!");
        }
        return wb;
    }

    public static Workbook getWorkbook(InputStream input, ExcelTypeEnum excelTypeEnum) {
        try {
            switch (excelTypeEnum) {
                case XLS:
                    return new HSSFWorkbook(input);
                case XLSX:
                    return new XSSFWorkbook(input);
                default:
                    throw new OperationException("不支持的文件格式！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Workbook getWorkbook1(InputStream input, ExcelTypeEnum excelTypeEnum) {
        switch (excelTypeEnum) {
            case XLS:
                return newInstance(input, ExcelTypeUtils::getXlsWorkbook, ExcelTypeUtils::getXlsxWorkbook);
            case XLSX:
                return newInstance(input, ExcelTypeUtils::getXlsxWorkbook, ExcelTypeUtils::getXlsWorkbook);
            default:
                throw new OperationException("不支持的文件格式！");
        }
    }

    private static Workbook getXlsWorkbook(InputStream input){
        try {
            return new HSSFWorkbook(input);
        } catch (Exception e) {
            throw new OperationException(e.getMessage());
        }
    }

    private static Workbook getXlsxWorkbook(InputStream input){
        try {
            return new XSSFWorkbook(input);
        } catch (Exception e) {
            throw new OperationException(e.getMessage());
        }
    }

    public static <T, R> R newInstance(T param, Function<T, R> function, Function<T, R> next) {
        try {
            return function.apply(param);
        } catch (Exception e) {
            return next.apply(param);
        }
    }

    public static Sheet getSheet(InputStream input, ExcelTypeEnum excelTypeEnum, Integer sheetAt) {
        Workbook workbook = getWorkbook(input, excelTypeEnum);
        if (workbook == null) {
            throw new OperationException("没有找到工作表！");
        }
        return workbook.getSheetAt(sheetAt);
    }

    public static ExcelTypeEnum getExcelType(String fileName) {
        if (fileName.endsWith(ExcelTypeEnum.XLS.getSuffix())) {
            return ExcelTypeEnum.XLS;
        } else if (fileName.endsWith(ExcelTypeEnum.XLSX.getSuffix())) {
            return ExcelTypeEnum.XLSX;
        } else {
            throw new OperationException("Not support this excelType!");
        }
    }

    public static Sheet getSheet(Workbook wb, String sheetName) {
        if (sheetName == null) {
            return wb.createSheet();
        }
        return wb.createSheet(sheetName);
    }

    public static Sheet getSheet(Workbook wb, Integer sheetAt) {
        try {
            return wb.getSheetAt(sheetAt);
        } catch (Exception e) {
            return wb.createSheet();
        }
    }

    public static Sheet getSheet(Workbook wb, Attribute sheetAttribute) {
        Sheet sheet;
        if (sheetAttribute != null) {
            String sheetName = sheetAttribute.getValue();
            sheet = getSheet(wb, sheetName);
        } else {
            sheet = wb.createSheet();
        }
        return sheet;
    }

    public static Row getRow(Sheet sheet, ExcelTypeEnum typeEnum, Integer rownum) {
        Row row;
        if (typeEnum == ExcelTypeEnum.XLS) {
            row = ((HSSFSheet) sheet).createRow(rownum);
        } else if (typeEnum == ExcelTypeEnum.XLSX) {
            row = ((XSSFSheet) sheet).createRow(rownum);
        } else {
            throw new OperationException("Not support this excelType!");
        }
        return row;
    }

    public static DataValidation getDataValidation(Sheet sheet, CellRangeAddressList regions, String[] explicitListValues) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        //设置下拉框数据
        DataValidationConstraint constraint = helper.createExplicitListConstraint(explicitListValues);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        //处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        return dataValidation;
    }

    public static HSSFDataValidation getHssfDataValidation(CellRangeAddressList regions, String[] explicitListValues) {
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(explicitListValues);
        return new HSSFDataValidation(regions, constraint);
    }
}
