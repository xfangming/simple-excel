package com.tobiasy.simple.api;

import com.tobiasy.simple.anno.ExportUseAnnotation;
import com.tobiasy.simple.anno.ImportUseAnnotation;
import com.tobiasy.simple.bean.ExcelFont;
import com.tobiasy.simple.bean.OrderField;
import com.tobiasy.simple.bean.SheetResult;
import com.tobiasy.simple.bean.WorkbookResult;
import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.excel.*;
import com.tobiasy.simple.exception.OperationException;
import com.tobiasy.simple.utils.ArrayUtils;
import com.tobiasy.simple.utils.ClassHelper;
import com.tobiasy.simple.utils.CollectionUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author tobiasy
 * @date 2019/11/11
 */
public class ExportCreator<T> {
    private ExcelHeader<T, Object> header;
    private ExportBody<T> body = new ExportBody<>();
    private ExcelTypeEnum excelType = ExportBuilder.DEFAULT_TYPE;
    private Workbook workbook;
    private File xmlFile;
    private ExportTemplate exportTemplate;

    public ExportCreator<T> header(ExcelHeader<T, Object> header) {
        this.header = header;
        return this;
    }

    public ExportCreator<T> header(Function<T, Object>[] functions) {
        this.header = ExcelHeader.of(functions);
        return this;
    }

    public ExportCreator<T> header(String[] attrs) {
        this.header = ExcelHeader.of(attrs);
        return this;
    }

    public ExportCreator<T> body(ExportBody<T> body) {
        this.body = body;
        return this;
    }

    public ExportCreator<T> body(List<T> data, Position position) {
        this.body = new ExportBody<>(data, position);
        return this;
    }

    public ExportCreator<T> body(List<T> data) {
        this.body = new ExportBody<>(data, new Position());
        return this;
    }

    public ExportCreator<T> type(ExcelTypeEnum excelType) {
        this.excelType = excelType;
        return this;
    }

    public ExportCreator<T> xmlFile(File file) {
        this.xmlFile = file;
        return this;
    }

    public ExportCreator<T> template(ExportTemplate exportTemplate) {
        this.exportTemplate = exportTemplate;
        return this;
    }

    public ExportCreator<T> template(String head, String[] titles) {
        this.exportTemplate = ExportTemplate.of(head, titles);
        return this;
    }

    public ExportCreator<T> template(String[] titles, ExcelFont excelFont) {
        this.exportTemplate = ExportTemplate.of(titles, excelFont);
        return this;
    }

    private void headerEmptyAssert() {
        if (header == null) {
            throw new OperationException("还没设置头部参数！");
        }
        if (header.getAttrs() == null && header.getFunctions() == null) {
            throw new OperationException("请指定匹配的属性或属性函数！");
        }
    }

    public ExportCreator<T> build() {
        Position position = body.getPosition();
        Consumer<WorkbookResult> consumer = (result) -> {
            Workbook workbook = result.getWorkbook();
            CellStyle cellStyle = TemplateBuilder.getContentCellStyle(workbook);
            Sheet sheetAt = workbook.getSheetAt(0);
            Integer startRow = position.getStartRow();
            List<T> data = body.getData();
            if (data != null) {
                for (T t : data) {
                    Row row = sheetAt.createRow(startRow++);
                    headerEmptyAssert();
                    if (header.isFunction()) {
                        RowUtils.doRow(row, t, header.getFunctions(), position.getStartCol(), cellStyle);
                    } else {
                        RowUtils.doRow(row, t, header.getAttrs(), position.getStartCol(), cellStyle);
                    }
                }
            }
        };
        return build(consumer);
    }

    public ExportCreator<T> build(Consumer<WorkbookResult> consumer) {
        this.workbook = ExportBuilder.getWorkBook(xmlFile, excelType, consumer);
        return this;
    }

    public ExportCreator<T> buildXml() {
        Position position = body.getPosition();
        Consumer<WorkbookResult> consumer = (result) -> {
            Workbook workbook = result.getWorkbook();
            Map<Integer, SheetResult> sheetMap = result.getSheetMap();
            Sheet sheetAt = workbook.getSheetAt(0);
            SheetResult sheetResult = sheetMap.get(0);
            CellStyle cellStyle = sheetResult.getCellStyle();
            List<OrderField> orderField = sheetResult.getOrderField();
            Integer startRow = position.getStartRow();
            List<T> data = body.getData();
            if (data != null) {
                for (T t : data) {
                    Row row = sheetAt.createRow(startRow++);
                    RowUtils.doRow(row, t, orderField, cellStyle);
                }
            }
        };
        return build(consumer);
    }

    public ExportCreator<T> buildXmlAnnotation() {
        if (xmlFile == null) {
            throw new OperationException("请指定xml导出文件");
        }
        this.workbook = ExportBuilder.getWorkBook(xmlFile, excelType, ArrayUtils.asArray(body.getData()));
        return this;
    }

    public ExportCreator<T> buildOnlyAnnotation() {
        List<T> data = body.getData();
        if (data == null) {
            throw new OperationException("导出数据不能为null！");
        }
        Class<?> clazz = ClassHelper.getClass(data);
        List<OrderField> orderFields = ExcelAnnotation.getFieldOrderFunction(clazz);
        String[] titles = orderFields.stream().map(OrderField::getName).toArray(String[]::new);
        ExportUseAnnotation exportAnno = ExcelAnnotation.getExportUseAnnotation(clazz);
        Position position = AnnotationBuilder.getPosition(exportAnno);
        body.setPosition(position);
        ExportUseAnnotation exportUseAnnotation = ExcelAnnotation.getExportUseAnnotation(clazz);
        String head = null;
        if (exportUseAnnotation == null && exportTemplate == null) {
            throw new OperationException("请先配置导出模板！");
        }
        if (exportUseAnnotation != null) {
            head = exportUseAnnotation.name();
        }
        if (exportTemplate != null) {
            head = exportTemplate.getHead();
            titles = exportTemplate.getTitles();
        }
        this.workbook = AnnotationBuilder.getWorkbook(head, titles, orderFields, data, excelType);
        return this;
    }

    public ExportCreator<T> buildTemplate() {
        if (exportTemplate == null) {
            throw new OperationException("请先设置导出模板！");
        }
        List<T> data = body.getData();
        if (exportTemplate.isHasFont()) {
            this.workbook = TemplateBuilder.getWorkbook(exportTemplate.getTitles(), exportTemplate.getExcelFont());
        } else {
            if (CollectionUtils.notEmpty(data)) {
                headerEmptyAssert();
            }
            this.workbook = TemplateBuilder.getWorkbook(exportTemplate.getHead(), exportTemplate.getTitles(), header, data, excelType);
        }
        return this;
    }

    public void toResponse(HttpServletResponse response, String fileName) {
        DownloadUtil.export(response, workbook, fileName);
    }

    public void toExcel(File file) {
        ExcelUtils.createExcel(workbook, file);
    }

}
