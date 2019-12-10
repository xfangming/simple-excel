package com.tobiasy.simple.excel;

import com.tobiasy.simple.enums.ExcelTypeEnum;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleDataBuilderTest {

    @Test
    public void getExcelData() {
        File file = new File("F:/test/excel/销售明细表.xlsx");
        List<String> list = SimpleDataBuilder.getExcelData(file, 0, 4, 3);
        List<String> names = list.stream().distinct().collect(Collectors.toList());
        System.out.println("共有" + names.size() + "所学校");
        Workbook workbook = ExcelTypeUtils.getWorkbook(ExcelTypeEnum.XLSX);
        Sheet sheet = workbook.createSheet();
        for (int i = 0; i < names.size(); i++) {
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(names.get(i));
        }
        ExcelUtils.createExcel(workbook, new File("F://test/excel/school.xlsx"));
    }
}