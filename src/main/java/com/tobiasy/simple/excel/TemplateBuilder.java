package com.tobiasy.simple.excel;

import com.tobiasy.simple.anno.ExportUseAnnotation;
import com.tobiasy.simple.api.ExcelHeader;
import com.tobiasy.simple.bean.ExcelFont;
import com.tobiasy.simple.bean.ModifierEnum;
import com.tobiasy.simple.bean.OrderField;
import com.tobiasy.simple.constants.ExcelConstants;
import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.exception.OperationException;
import com.tobiasy.simple.utils.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @author tobiasy
 * @date 2018/11/26
 */
public class TemplateBuilder {
    private static final ExcelTypeEnum DEFAULT_EXCEL_TYPE = ExcelTypeEnum.XLS;

    /**
     * 快速导出
     */
    public static <T> void export(List<T> list, HttpServletResponse response, String fileName) {
        T object = list.get(0);
        Class c = object.getClass();
        Workbook wb;
        if (fileName.endsWith(ExcelTypeEnum.XLS.getSuffix())) {
            wb = new HSSFWorkbook();
        } else if (fileName.endsWith(ExcelTypeEnum.XLSX.getSuffix())) {
            wb = new XSSFWorkbook();
        } else {
            throw new OperationException("错误的文件名！");
        }
        Sheet sheet = wb.createSheet(c.getSimpleName());
        sheet.setDefaultColumnWidth(15);
        Row row;
        Cell cell;
        row = sheet.createRow(0);
        List<Field> fs = ReflectUtils.getDeclaredFieldsNoModifier(c, ModifierEnum.STATIC, ModifierEnum.FINAL);
        int n = fs.size();
        for (int i = 0; i < n; i++) {
            Field field = fs.get(i);
            cell = row.createCell(i);
            cell.setCellValue(field.getName());
            cell.setCellStyle(getDefaultCellStyle(wb));
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            Object obj = list.get(i);
            for (int j = 0; j < n; j++) {
                cell = row.createCell(j);
                try {
                    Object o = ReflectUtils.invoke(obj, Generate.toGetter(fs.get(j).getName()));
                    Class<?> type = fs.get(j).getType();
                    if (o != null) {
                        if (type == Date.class) {
                            cell.setCellValue(DateUtils.format(o));
                        } else {
                            cell.setCellValue(o.toString());
                        }
                    }
                    cell.setCellStyle(getContentCellStyle(wb));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        DownloadUtil.export(response, wb, fileName);
    }

    /**
     * 模板导出
     */
    public static void export(String head, String[] headers, HttpServletResponse response, String fileName) {
        Workbook workbook = getWorkbook(head, headers);
        DownloadUtil.export(response, workbook, fileName);
    }

    public static <T, R> void export(String head, String[] headers, Function<T, R>[] functions, List<T> list, HttpServletResponse response, String fileName) {
        Workbook workbook = getWorkbook(head, headers, functions, list);
        DownloadUtil.export(response, workbook, fileName);
    }

    public static Workbook getWorkbook(String[] headers) {
        return getWorkbook(null, headers);
    }

    /**
     * 通过数据生成excel文件对象
     *
     * @param headers   头部
     * @param functions 属性
     * @param list      数据
     * @return
     */
    public static <T> Workbook getWorkbook(String[] headers, Function<T, Object>[] functions, List<T> list) {
        return getWorkbook(null, headers, functions, list);
    }

    public static Workbook getWorkbook(String head, String[] headers) {
        return getWorkbook(head, headers, new Function[]{}, null);
    }

    public static <T, R> Workbook getWorkbook(String head, String[] headers,
                                              Function<T, R>[] functions, List<T> list) {
        return getWorkbook(head, headers, functions, list, DEFAULT_EXCEL_TYPE);
    }

    /**
     * 注解模板方式
     *
     * @return
     */
    public static <T> Workbook getWorkbook(List<T> list, ExcelTypeEnum excelTypeEnum) {
        Class<?> clazz = ClassHelper.getClass(list);
        ExportUseAnnotation exportUseAnnotation = ExcelAnnotation.getExportUseAnnotation(clazz);
        if (exportUseAnnotation == null) {
            throw new OperationException("没有扫描到@ExportUseAnnotation注解！");
        }
        String head = exportUseAnnotation.name();
        int startRow = exportUseAnnotation.firstRow();
        int sheetAt = 0;
        List<OrderField> orderField = ExcelAnnotation.getFieldOrderFunction(clazz);
        if (orderField == null) {
            throw new OperationException("没有设置导出属性，不能操作！若使用注解设置请在%s中设置使用注解配置方式（添加@ExportUseAnnotation注解）", ClassHelper.getClass(list));
        }
        String[] titles = orderField.stream().map(OrderField::getName).toArray(String[]::new);
        Workbook workbook = getWorkbook(head, titles, excelTypeEnum);
        CellStyle cellStyle = getContentCellStyle(workbook);
        Sheet sheet = workbook.getSheetAt(sheetAt);
        for (T t : list) {
            Row row = sheet.createRow(startRow++);
            RowUtils.doRow(row, t, orderField, cellStyle);
        }
        return workbook;
    }

    public static Workbook getWorkbook(String head, String[] headers, ExcelTypeEnum excelTypeEnum) {
        return getWorkbook(head, headers, ExcelHeader.empty(), null, excelTypeEnum);
    }

    /**
     * 函数模板方式
     *
     * @return
     */
    public static <T, R> Workbook getWorkbook(String head, String[] headers,
                                              Function<T, R>[] functions,
                                              List<T> list, ExcelTypeEnum excelTypeEnum) {
        return getWorkbook(head, headers, ExcelHeader.of(functions), list, excelTypeEnum);
    }

    /**
     * 字符模板方式
     *
     * @param head          大标题
     * @param headers       列标题
     * @param attr          属性
     * @param list          数据
     * @param excelTypeEnum excel类型
     * @param <T>
     * @return
     */
    public static <T> Workbook getWorkbook(String head, String[] headers, String[] attr,
                                           List<T> list, ExcelTypeEnum excelTypeEnum) {
        return getWorkbook(head, headers, ExcelHeader.of(attr), list, excelTypeEnum);
    }

    public static <T, R> Workbook getWorkbook(String head, String[] headers,
                                              ExcelHeader<T, R> header,
                                              List<T> list, ExcelTypeEnum excelTypeEnum) {
        Workbook workbook = ExcelTypeUtils.getWorkbook(excelTypeEnum);
        Sheet sheet = workbook.createSheet(ExcelConstants.DEFAULT_SHEET_NAME);
        Integer start = doHeader(sheet, head, headers);
        CellStyle contentCellStyle = getContentCellStyle(workbook);
        Row row;
        if (list != null) {
            for (T t : list) {
                row = sheet.createRow(start++);
                if (header.isFunction()) {
                    Function<T, R>[] functions = BeanUtils.doFieldNotNull(header, ExcelHeader::getFunctions, "属性函数不能为空！");
                    RowUtils.doRow(row, t, functions, null, contentCellStyle);
                } else {
                    String[] attrs = BeanUtils.doFieldNotNull(header, ExcelHeader::getAttrs, "属性不能为空！");
                    RowUtils.doRow(row, t, attrs, null, contentCellStyle);
                }
            }
        }
        return workbook;
    }

    public static Integer doHeader(Sheet sheet, String head, String[] headers) {
        Workbook workbook = sheet.getWorkbook();
        for (int i = 0; i < headers.length; i++) {
            short width = 4500;
            sheet.setColumnWidth(i, width);
        }
        sheet.setDefaultColumnWidth(15);
        Row row;
        Cell cell;
        CellStyle styleHead = workbook.createCellStyle();
        styleHead.setFont(getHeadFont(workbook));
        styleHead.setAlignment(HorizontalAlignment.CENTER);
        styleHead.setVerticalAlignment(VerticalAlignment.CENTER);
        CellStyle style = workbook.createCellStyle();
        Font font = getTitleFont(workbook);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        int start = 0;
        short rowHeight = 500;
        if (StringUtils.notEmpty(head)) {
            row = sheet.createRow(start++);
            row.setHeight(rowHeight);
            Cell cellHead = row.createCell(0);
            cellHead.setCellStyle(styleHead);
            cellHead.setCellValue(head);
            if (headers.length > 1) {
                CellRangeAddress cellRange = new CellRangeAddress(
                        0, 0,
                        0, headers.length - 1);
                sheet.addMergedRegion(cellRange);

            }
        }
        row = sheet.createRow(start++);
        row.setHeight(rowHeight);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
        return start;
    }

    /**
     * 纯模板方式 - 快捷模板
     *
     * @param headers     列标题
     * @param definedFont 字体
     * @return
     */
    public static Workbook getWorkbook(String[] headers, ExcelFont definedFont) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(ExcelConstants.DEFAULT_SHEET_NAME);
        Row row = sheet.createRow(0);
        row.setHeight((short) 500);
        CellStyle style = workbook.createCellStyle();
        Font font;
        if (definedFont != null) {
            font = getFont(workbook, definedFont);
        } else {
            font = getTitleFont(workbook);
        }
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, 4500);
            String header = headers[i];
            Cell cell = row.createCell(i);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(header);
            cell.setCellStyle(style);
        }
        return workbook;
    }

    private static Font getFont(Workbook workbook, ExcelFont definedFont){
        Font font = null;
        if (definedFont != null) {
            font = workbook.createFont();
            font.setBold(definedFont.getBold());
            if (definedFont.getFontName() != null) {
                font.setFontName(definedFont.getFontName());
            }
            if (definedFont.getFontHeightInPoints() != null) {
                font.setFontHeightInPoints(definedFont.getFontHeightInPoints());
            }
            if (definedFont.getColor() != null) {
                font.setColor(definedFont.getColor());
            }
        }
        return font;
    }

    public static Font getTitleFont(Workbook workbook) {
        return getFont(workbook, true, null, (short) 10, (short) 8);
    }

    public static Font getHeadFont(Workbook workbook) {
        return getFont(workbook, true, "黑体", (short) 12, (short) 8);
    }

    public static Font getFont(Workbook workbook, Boolean isBold, String fontName, Short fontHeight, Short color) {
        Font font = workbook.createFont();
        if (isBold) {
            font.setBold(true);
        }
        if (fontName != null) {
            font.setFontName(fontName);
        }
        if (fontHeight != null) {
            font.setFontHeightInPoints(fontHeight);
        }
        if (color != null) {
            font.setColor(color);
        }
        return font;
    }

    public static CellStyle getDefaultCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    public static CellStyle getContentCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }
} 