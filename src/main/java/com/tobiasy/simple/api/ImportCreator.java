package com.tobiasy.simple.api;

import com.tobiasy.simple.anno.ImportUseAnnotation;
import com.tobiasy.simple.bean.OrderField;
import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.excel.ExcelAnnotation;
import com.tobiasy.simple.excel.ExcelHelper;
import com.tobiasy.simple.excel.ExportBuilder;
import com.tobiasy.simple.excel.ImportBuilder;
import com.tobiasy.simple.exception.OperationException;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author tobiasy
 * @date 2019/11/12
 */
public class ImportCreator<T> {
    private ExcelHeader<T, Object> header;
    private ImportBody body = new ImportBody(new Position());
    private ExcelTypeEnum excelType = ExportBuilder.DEFAULT_TYPE;
    private File inputFile;
    private List<T> data;
    private List<Map<Integer, String>> dataMap;


    public ImportCreator<T> header(ExcelHeader<T, Object> header) {
        this.header = header;
        return this;
    }

    public ImportCreator<T> header(BiConsumer<T, String>[] attributes) {
        this.header = ExcelHeader.of(attributes);
        return this;
    }

    public ImportCreator<T> header(String[] attrs) {
        this.header = ExcelHeader.of(attrs);
        return this;
    }

    public ImportCreator<T> body(ImportBody body) {
        this.body = body;
        return this;
    }

    public ImportCreator<T> body(Function<String, Object>[] functions, Position position) {
        this.body = new ImportBody(functions, position);
        return this;
    }

    public ImportCreator<T> body(Position position) {
        this.body = new ImportBody(position);
        return this;
    }

    public ImportCreator<T> inputFile(File inputFile) {
        this.inputFile = inputFile;
        return this;
    }

    public ImportCreator<T> type(ExcelTypeEnum excelType) {
        this.excelType = excelType;
        return this;
    }

    private void check() {
        if (inputFile == null) {
            throw new OperationException("请先指定导入文件！");
        }
        InputStream inputStream = ExcelHelper.TO_INPUT_STREAM.apply(inputFile);
        if (inputStream == null) {
            throw new OperationException("找不到文件！");
        }
        if (body == null) {
            throw new OperationException("请指定导入数据主体！");
        }
        boolean attrEmpty = (header.getAttrs() == null && header.getAttributes() == null);
        if (header == null || attrEmpty) {
            throw new OperationException("请指定匹配的属性或属性函数！");
        }
    }

    public ImportCreator<T> build(Supplier<T> newInstance) {
        check();
        InputStream inputStream = ExcelHelper.TO_INPUT_STREAM.apply(inputFile);
        Position position = body.getPosition();
        dataMap = ImportBuilder.getData(inputStream, excelType, position.getSheetAt(), position.getStartRow());
        if (header.isFunction()) {
            data = ImportBuilder.parse(dataMap, header.getAttributes(), newInstance);
        } else {
            if (body.getFunctions() == null) {
                throw new OperationException("属性转换函数不能为空！");
            }
            data = ImportBuilder.parse(dataMap, header.getAttrs(), body.getFunctions(), newInstance);
        }
        return this;
    }

    public ImportCreator<T> buildAnnotation(Supplier<T> newInstance) {
        Class<?> clazz = newInstance.get().getClass();
        ImportUseAnnotation importType = ExcelAnnotation.getImportType(clazz);
        Position position = ImportBuilder.getPosition(importType);
        dataMap = ImportBuilder.getData(
                ExcelHelper.toInputStream(inputFile),
                excelType, position.getSheetAt(), position.getStartRow());
        List<OrderField> fields = ExcelAnnotation.getFieldOrderFunction(clazz);
        data = ImportBuilder.parse(dataMap, fields, newInstance, importType);
        return this;
    }

    public List<Map<Integer, String>> toMap() {
        if (dataMap == null) {
            throw new OperationException("请先执行build()方法！");
        }
        return dataMap;
    }

    public List<T> toList() {
        if (data == null) {
            throw new OperationException("请先执行build()方法！");
        }
        return data;
    }

    @Deprecated
    public List<T> toSimpleList(Supplier<T> newInstance) {
        check();
        InputStream inputStream = ExcelHelper.TO_INPUT_STREAM.apply(inputFile);
        Position position = body.getPosition();
        if (header.isFunction()) {
            data = ImportBuilder.getList(
                    inputStream, position.getSheetAt(), position.getStartRow(),
                    header.getAttributes(),
                    newInstance, excelType);
        } else {
            throw new OperationException("用属性数组导出时，必须在body()中使用属性函数！");
        }
        return data;
    }


}
