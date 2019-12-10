package com.tobiasy.simple.excel;

import com.tobiasy.simple.anno.ExportUseAnnotation;
import com.tobiasy.simple.anno.ImportUseAnnotation;
import com.tobiasy.simple.api.Position;
import com.tobiasy.simple.bean.OrderField;
import com.tobiasy.simple.constants.ExcelConstants;
import com.tobiasy.simple.enums.ExcelTypeEnum;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.logging.Logger;

import static com.tobiasy.simple.excel.TemplateBuilder.getContentCellStyle;

/**
 * @author tobiasy
 * @date 2019/11/20
 */
public class AnnotationBuilder {

    private static Logger log = Logger.getLogger("AnnotationBuilder");

    public static <T> Workbook getWorkbook(String head, String[] headers,
                                            List<OrderField> orderFields,
                                            List<T> list, ExcelTypeEnum excelTypeEnum) {
        Workbook workbook = ExcelTypeUtils.getWorkbook(excelTypeEnum);
        Sheet sheet = workbook.createSheet(ExcelConstants.DEFAULT_SHEET_NAME);
        Integer start = TemplateBuilder.doHeader(sheet, head, headers);
        CellStyle contentCellStyle = getContentCellStyle(workbook);
        Row row;
        if (list != null) {
            for (T t : list) {
                row = sheet.createRow(start++);
                RowUtils.doRow(row, t, orderFields, contentCellStyle);
            }
        }
        return workbook;
    }

    public static Position getPosition(ExportUseAnnotation exportAnno) {
        int sheetAt = 0;
        int firstRow = 0;
        if (exportAnno != null) {
            sheetAt = exportAnno.sheetAt();
            firstRow = exportAnno.firstRow();
        } else {
            log.info("当未指定导出函数时可以使用@ExportUseAnnotation设置参数[sheetAt, firstRow]，缺省值均为0");
        }
        return Position.of(sheetAt, firstRow, 0);
    }
}
