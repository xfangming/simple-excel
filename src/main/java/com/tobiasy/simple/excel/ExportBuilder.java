package com.tobiasy.simple.excel;

import com.tobiasy.simple.anno.ExportUseAnnotation;
import com.tobiasy.simple.bean.ColGroup;
import com.tobiasy.simple.bean.OrderField;
import com.tobiasy.simple.bean.SheetResult;
import com.tobiasy.simple.bean.WorkbookResult;
import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.exception.OperationException;
import com.tobiasy.simple.utils.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Excel导出核心处理类
 *
 * @author tobiasy
 */
public class ExportBuilder {

    public static final ExcelTypeEnum DEFAULT_TYPE = ExcelTypeEnum.XLS;

    /*
     * ******************************* 模板 *************************************
     */

    /**
     * 函数模板方式
     *
     * @return
     */
    public static <T, R> Workbook getWorkbook(String head, String[] headers,
                                              Function<T, R>[] functions,
                                              List<T> list, ExcelTypeEnum excelTypeEnum) {
        return TemplateBuilder.getWorkbook(head, headers, functions, list, excelTypeEnum);
    }

    /**
     * 注解模板方式
     * 对象中需要 @ExportUseAnnotation、@ExcelField注解
     *
     * @return
     */
    public static <T> Workbook getWorkbook(List<T> list, ExcelTypeEnum excelTypeEnum) {
        return TemplateBuilder.getWorkbook(list, excelTypeEnum);
    }

    /*
     * ******************************* XML *************************************
     */

    public static <T> void export(File xmlFile,
                                  HttpServletResponse response,
                                  String fileName,
                                  List<T> lists) {
        List<T>[] arrays = ArrayUtils.asArray(lists);
        Workbook workBook = getWorkBook(xmlFile, DEFAULT_TYPE, arrays);
        DownloadUtil.export(response, workBook, fileName);
    }

    public static <T> void export(File xmlFile,
                                  HttpServletResponse response,
                                  String fileName,
                                  List<T>... lists) {
        Workbook workBook = getWorkBook(xmlFile, DEFAULT_TYPE, lists);
        DownloadUtil.export(response, workBook, fileName);
    }

    public static <T> void export(File xmlFile,
                                  HttpServletResponse response,
                                  String fileName,
                                  List<T> list,
                                  int sheetSize) {
        List<List<T>> incise = CollectionUtils.incise(list, sheetSize);
        IntFunction<List<T>[]> dataFunc = List[]::new;
        List<T>[] arrays = ArrayUtils.toArray(incise, dataFunc);
        Workbook workBook = getWorkBook(xmlFile, DEFAULT_TYPE, arrays);
        DownloadUtil.export(response, workBook, fileName);
    }

    public static <T> void export(File xmlFile,
                                  ExcelTypeEnum typeEnum,
                                  HttpServletResponse response,
                                  String fileName,
                                  List<T>... lists) {
        Workbook workBook = getWorkBook(xmlFile, typeEnum, lists);
        DownloadUtil.export(response, workBook, fileName);
    }

    /**
     * excel导出核心方法
     * 对象中通过注解配置需要显示的属性及其序列
     *
     * @param xmlFile  xml文件
     * @param tempFile 保存文件
     * @param list     集合
     * @param <T>      集合泛型
     * @return
     */
    public static <T> boolean createExcel(File xmlFile, File tempFile, List<T> list) {
        List<T>[] arrays = ArrayUtils.asArray(list);
        return createExcel(xmlFile, tempFile, DEFAULT_TYPE, arrays);
    }

    public static <T> boolean createExcel(File xmlFile, File temFile,
                                          ExcelTypeEnum typeEnum,
                                          List<T>... list) {
        Workbook workBook = getWorkBook(xmlFile, typeEnum, list);
        return ExcelUtils.createExcel(workBook, temFile);
    }

    public static boolean createExcel(File xmlFile, File tempFile, Consumer<WorkbookResult> consumer) {
        Workbook workBook = getWorkBook(xmlFile, DEFAULT_TYPE, consumer);
        return ExcelUtils.createExcel(workBook, tempFile);
    }

    public static <T> boolean createExcel(File xmlFile, File temFile,
                                          List<T> list, String[] attrs,
                                          Integer firstRow) {
        return createExcel(xmlFile, temFile, list, attrs, firstRow, 0, 0, DEFAULT_TYPE);
    }

    public static <T> boolean createExcel(File xmlFile, File temFile,
                                          List<T> list, String[] attrs,
                                          Integer firstRow,
                                          ExcelTypeEnum typeEnum) {
        return createExcel(xmlFile, temFile, list, attrs, firstRow, 0, 0, typeEnum);
    }

    public static <T> boolean createExcel(File xmlFile, File temFile,
                                          List<T> list, String[] attrs,
                                          Integer sheetAt, Integer firstRow, Integer firstCol,
                                          ExcelTypeEnum typeEnum) {
        Workbook workBook = getWorkBook(xmlFile, attrs, sheetAt, firstRow, firstCol, typeEnum, list);
        return ExcelUtils.createExcel(workBook, temFile);
    }

    /**
     * 根据excel数据模板xml文件xmlFile将模板写入临时文件tempFile中
     *
     * @param xmlFile  xml文件
     * @param tempFile 临时文件
     */
    public static boolean createExcel(File xmlFile, File tempFile) {
        return ExcelUtils.createExcel(getWorkBook(xmlFile), tempFile);
    }

    public static boolean createExcel(File xmlFile, File tempFile, ExcelTypeEnum typeEnum) {
        return ExcelUtils.createExcel(getWorkbookResult(xmlFile, typeEnum).getWorkbook(), tempFile);
    }

    public static boolean createExcel(String xmlFileName, String tempFilePath) {
        return ExcelUtils.createExcel(
                getWorkBook(new ClassLoaderUtils().getFile(xmlFileName)),
                new File(tempFilePath)
        );
    }

    public static <T> Workbook getWorkBook(File xmlFile,
                                           List<T> list) {
        List<T>[] arrays = ArrayUtils.asArray(list);
        return getWorkBook(xmlFile, DEFAULT_TYPE, arrays);
    }

    public static Workbook getWorkBook(File xmlFile, ExcelTypeEnum typeEnum, Consumer<WorkbookResult> consumer) {
        if (xmlFile == null) {
            throw new OperationException("没有找到匹配的XML配置文件！");
        }
        if (typeEnum == null) {
            typeEnum = DEFAULT_TYPE;
        }
        WorkbookResult result = getWorkbookResult(xmlFile, typeEnum);
        if (consumer != null) {
            consumer.accept(result);
        }
        return result.getWorkbook();
    }

    /**
     * 通过自定义导出属性，自然顺序
     *
     * @return
     */
    public static <T> Workbook getWorkBook(File xmlFile, String[] attrs,
                                           Integer sheetAt, Integer firstRow, Integer firstCol,
                                           ExcelTypeEnum typeEnum,
                                           List<T> list) {
        Consumer<WorkbookResult> consumer = (result) -> {
            Workbook workbook = result.getWorkbook();
            CellStyle cellStyle = TemplateBuilder.getContentCellStyle(workbook);
            Sheet sheet = workbook.getSheetAt(sheetAt);
            Integer startRow = firstRow;
            for (T t : list) {
                Row row = sheet.createRow(startRow++);
                RowUtils.doRow(
                        row, t,
                        attrs,
                        firstCol, cellStyle);
            }
        };
        return getWorkBook(xmlFile, typeEnum, consumer);
    }

    @SafeVarargs
    public static <T> Workbook getWorkBook(File xmlFile,
                                           Function<T, Object>[] functions,
                                           Integer sheetAt, Integer firstRow, Integer firstCol,
                                           ExcelTypeEnum typeEnum,
                                           List<T>... lists) {
        Consumer<WorkbookResult> consumer = (result) -> {
            Workbook workbook = result.getWorkbook();
            List<ColGroup> colGroups = result.getColGroups();
            CellStyle cellStyle = TemplateBuilder.getContentCellStyle(workbook);
            for (int i = sheetAt; i < lists.length + sheetAt; i++) {
                List<T> list = lists[i];
                Sheet sheet = ExcelTypeUtils.getSheet(workbook, i);
                if (colGroups != null) {
                    for (ColGroup colGroup : colGroups) {
                        sheet.setColumnWidth(colGroup.getIndex(), colGroup.getWidth());
                    }
                }
                Integer startRow;
                if (i == sheetAt) {
                    startRow = firstRow;
                } else {
                    startRow = 0;
                }
                for (T t : list) {
                    Row row = sheet.createRow(startRow++);
                    RowUtils.doRow(
                            row, t,
                            functions,
                            firstCol, cellStyle);
                }
            }
        };
        return getWorkBook(xmlFile, typeEnum, consumer);
    }

    /**
     * 注解配置方式
     *
     * @param xmlFile  xml文件
     * @param typeEnum excel文件类型
     * @param lists    数据
     */
    public static <T> Workbook getWorkBook(File xmlFile,
                                           ExcelTypeEnum typeEnum,
                                           List<T>... lists) {
        Consumer<WorkbookResult> consumer = (result) -> {
            Workbook workbook = result.getWorkbook();
            for (int sheetAt = 0; sheetAt < lists.length; sheetAt++) {
                List<T> list = lists[sheetAt];
                Class<?> clazz = ClassHelper.getClass(list);
                ExportUseAnnotation exportType = ExcelAnnotation.getExportUseAnnotation(clazz);
                Integer startRow;
                List<OrderField> orderField;
                CellStyle cellStyle;
                Short lineHeight = null;
                if (exportType == null) {
                    // xml配置
                    Map<Integer, SheetResult> sheetMap = result.getSheetMap();
                    SheetResult sheetResult = sheetMap.get(sheetAt);
                    if (sheetResult == null) {
                        throw new OperationException("'%s'文件中没有找到参数sheetAt=%s的<body>标签！", xmlFile.getName(), sheetAt);
                    }
                    startRow = sheetResult.getStartRow();
                    orderField = sheetResult.getOrderField();
                    cellStyle = sheetResult.getCellStyle();
                    lineHeight = sheetResult.getLineHeight();
                } else {
                    //注解
                    startRow = exportType.firstRow();
                    orderField = ExcelAnnotation.getFieldOrderFunction(clazz);
                    cellStyle = TemplateBuilder.getContentCellStyle(workbook);
                }
                if (orderField == null) {
                    throw new OperationException("没有设置导出属性，不能操作！若使用注解设置请在%s中设置使用注解配置方式（添加@ExportUseAnnotation注解）", ClassHelper.getClass(list));
                }
                Sheet sheet = workbook.getSheetAt(sheetAt);
                if (list != null) {
                    for (T t : list) {
                        Row row = sheet.createRow(startRow++);
                        if (lineHeight != null) {
                            row.setHeight(lineHeight);
                        }
                        RowUtils.doRow(row, t, orderField, cellStyle);
                    }
                }
            }

        };
        return getWorkBook(xmlFile, typeEnum, consumer);
    }

    /**
     * 创建模板文件
     *
     * @param xmlFile xml文件
     */
    public static Workbook getWorkBook(File xmlFile) {
        return getWorkbookResult(xmlFile, ExcelTypeEnum.XLS).getWorkbook();
    }

    public static Workbook getWorkbookIgnoreStyle(File xmlFile, ExcelTypeEnum typeEnum) {
        return getWorkbookResult(xmlFile, typeEnum).getWorkbook();
    }

    public static WorkbookResult getWorkbookResult(File xmlFile, ExcelTypeEnum typeEnum) {
        if (!xmlFile.exists()) {
            throw new OperationException("file '%s' not exists!", xmlFile.getName());
        }
        WorkbookResult workbookResult = new WorkbookResult();
        SAXBuilder builder = new SAXBuilder();
        Workbook wb;
        try {
            Document parse = builder.build(xmlFile);
            Element root = parse.getRootElement();
            wb = ExcelTypeUtils.getWorkbook(typeEnum);
            Element colAll = root.getChild("colgroup");
            if (colAll != null) {
                workbookResult.setColGroups(ConfigHandler.getColumnWidth(colAll));
            }
            List<Element> sheets = getChildren(root, "sheet");
            Map<Integer, SheetResult> map = new HashMap<>(sheets.size());
            for (int sheetAt = 0; sheetAt < sheets.size(); sheetAt++) {
                Element sheetEle = sheets.get(sheetAt);
                Attribute sheetAttribute = sheetEle.getAttribute("name");
                Sheet sheet = ExcelTypeUtils.getSheet(wb, sheetAttribute);
                Element colGroup = sheetEle.getChild("colgroup");
                if (colGroup != null) {
                    ConfigHandler.setColumnWidth(sheet, colGroup);
                }
                Element head = sheetEle.getChild("head");
                if (head != null) {
                    doTrElements(sheet, getChildren(head, "tr"));
                }
                SheetResult sheetResult = getBodyResult(wb, sheetEle);
                if (sheetResult != null) {
                    sheetResult.setSheetAt(sheetAt);
                    map.put(sheetAt, sheetResult);
                }
            }
            workbookResult.setSheetMap(map);
            workbookResult.setWorkbook(wb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbookResult;
    }

    private static void doTrElements(Sheet sheet, List<Element> trs) {
        int rownum = 0, column;
        Map<Integer, Set<Integer>> ignoredIndex = new HashMap<>(16);
        for (Element tr : trs) {
            Attribute height = tr.getAttribute("height");
            Row row = sheet.createRow(rownum);
            Integer titleHeight;
            if (height != null) {
                titleHeight = ConfigHandler.getNumberOutUnit(height);
                if (titleHeight != null) {
                    row.setHeight(Short.parseShort(titleHeight + ""));
                }
            }
            CellStyle cellStyle = ConfigHandler.getCellStyle(sheet.getWorkbook(), tr);
            int cellnum = 0;
            List<Element> tds = getChildren(tr, "td");
            for (column = 0; column < tds.size(); column++) {
                Element td = tds.get(column);
                int colspan = ConfigHandler.getIntAttribute(td, "colspan", 1);
                int rowspan = ConfigHandler.getIntAttribute(td, "rowspan", 1);
                while (ignore(rownum, cellnum, ignoredIndex)) {
                    cellnum++;
                }
                Cell cell = row.createCell(cellnum);
                String value = td.getValue();
                if (value != null) {
                    cell.setCellValue(value.trim());
                }
                cell.setCellStyle(cellStyle);
                if (colspan > 1 || rowspan > 1) {
                    int lastRow = rownum + rowspan - 1;
                    int lastCol = cellnum + colspan - 1;
                    if (lastCol > cellnum || lastRow > rownum) {
                        CellRangeAddress cellRange = new CellRangeAddress(
                                rownum, lastRow,
                                cellnum, lastCol);
                        sheet.addMergedRegion(cellRange);
                    }
                    if (lastRow > rownum) {
                        for (int j = rownum + 1; j <= lastRow; j++) {
                            Set<Integer> origin = ignoredIndex.get(j);
                            if (origin == null) {
                                origin = new HashSet<>();
                            }
                            add(cellnum, lastCol, origin);
                            ignoredIndex.put(j, origin);
                        }
                    }
                }
                cellnum += colspan;
            }
            rownum++;
        }
    }

    private static List<Element> getChildren(Element root, String name) {
        if (root == null) {
            return null;
        }
        return root.getChildren(name);
    }

    private static SheetResult getBodyResult(Workbook wb, Element sheetEle) {
        Element body = sheetEle.getChild("body");
        if (body != null) {
            Element tr = body.getChild("tr");
            SheetResult sheetResult = new SheetResult();
            if (tr != null) {
                Attribute height = tr.getAttribute("height");
                Integer titleHeight = ConfigHandler.getNumberOutUnit(height);
                if (titleHeight != null) {
                    short lineHeight = Short.parseShort(titleHeight + "");
                    sheetResult.setLineHeight(lineHeight);
                }
            }
            int startRow = ConfigHandler.getIntAttribute(tr, "firstrow", 0);
            List<OrderField> orderField = getOrderField(tr);
            sheetResult.setStartRow(startRow);
            sheetResult.setOrderField(orderField);
            CellStyle cellStyle = ConfigHandler.getCellStyle(wb, tr);
            sheetResult.setCellStyle(cellStyle);
            return sheetResult;
        }
        return null;
    }

    private static void add(Integer start, Integer end, Set<Integer> list) {
        for (int i = start; i <= end; i++) {
            list.add(i);
        }
    }

    private static boolean ignore(Integer rownum, Integer cellnum, Map<Integer, Set<Integer>> setMap) {
        Set<Integer> integers = setMap.get(rownum);
        if (integers == null) {
            return false;
        }
        return integers.contains(cellnum);
    }

    private static List<OrderField> getOrderField(Element trElement) {
        if (trElement == null) {
            return null;
        }
        List children = trElement.getChildren();
        List<OrderField> result = new ArrayList<>();
        for (Object child : children) {
            OrderField orderField = new OrderField();
            Element td = (Element) child;
            try {
                Attribute index = td.getAttribute("index");
                Attribute property = td.getAttribute("property");
                Attribute formula = td.getAttribute("formula");
                Attribute columnsize = td.getAttribute("columnsize");
                Attribute format = td.getAttribute("dateformat");
                if (index != null) {
                    orderField.setIndex(index.getIntValue());
                }
                if (property != null) {
                    orderField.setField(property.getValue());
                    orderField.setFieldGetter(Generate.toGetter(property.getValue()));
                }
                if (formula != null) {
                    String formulaName = formula.getValue();
                    formulaName = formulaName.replace("${rownum}", "%s").replaceAll("'", "\"");
                    orderField.setFormula(formulaName);
                }
                if (format != null) {
                    orderField.setDateFormat(format.getValue());
                }
                if (columnsize != null) {
                    int intValue = columnsize.getIntValue();
                    orderField.setColumnSize(intValue);
                } else {
                    orderField.setColumnSize(1);
                }
                result.add(orderField);
            } catch (DataConversionException e) {
                throw new OperationException(e.getMessage());
            }
        }
        return result;
    }

}
