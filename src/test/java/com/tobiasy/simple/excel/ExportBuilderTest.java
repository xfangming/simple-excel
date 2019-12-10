package com.tobiasy.simple.excel;

import com.tobiasy.simple.bean.ObjectData;
import com.tobiasy.simple.bean.Student;
import com.tobiasy.simple.bean.User;
import com.tobiasy.simple.bean.WorkbookResult;
import com.tobiasy.simple.constants.DateConst;
import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.utils.*;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

public class ExportBuilderTest {

    /**
     * 生成导出文件模板，不带数据
     */
    @Test
    public void createExcel() {
        String fileName = "simple-user";
        boolean f2 = ExportBuilder.createExcel(
                fileName + ".xml",
                "F:/test/excel/" + fileName + ".xls");
        System.out.println(f2);
    }

    /**
     * 拉姆达方式
     */
    @Test
    public void createExcelData() {
        List<Student> list = ObjectData.getStudentList(60000);
        Integer rowStart = 6;
        Integer firstCol = 0;
        String[] attrs = new String[]{"code", "name", "sex", "age", "birthday", "favourite"};
        Function<Student, Object>[] functions = ArrayUtils.asArray(
                Student::getCode, Student::getName,
                (u) -> u.getSex().getName(),
                Student::getAge,
                (u) -> DateUtils.format(u.getBirthday(), DateConst.SIMPLE_DATE_PATTERN),
                (u) -> u.getFavourite().getValue());
        Consumer<WorkbookResult> consumer = (result) -> {
            Workbook workbook = result.getWorkbook();
            CellStyle cellStyle = TemplateBuilder.getContentCellStyle(workbook);
            Sheet sheetAt = workbook.getSheetAt(0);
            Integer startRow = rowStart;
            for (Student student : list) {
                Row row = sheetAt.createRow(startRow++);
                //RowUtils.doRow(row, student, functions, firstCol, cellStyle);
                RowUtils.doRow(
                        row,
                        student,
                        attrs,
                        firstCol, cellStyle);
                Cell cell = row.createCell(7);
                CellUtils.addCellFormula(cell, "SUM(A%s:D%s)", startRow, startRow);
            }
        };
        Workbook workBook = ExportBuilder.getWorkBook(
                ClassLoaderUtils.getLoaderFile("simple-student.xml"),
                ExcelTypeEnum.XLSX,
                consumer);
        ExcelUtils.createExcel(workBook, new File("F:/test/excel/simpleExcelStudent.xls"));
    }

    /**
     * 字符XML方式
     */
    @Test
    public void createExcelList() {
        List<Student> list = ObjectData.getStudentList(60000);
        Integer firstRow = 3;
        String[] attrs = new String[]{"code", "name", "sex", "age", "birthday", "favourite"};
        Workbook workBook = ExportBuilder.getWorkBook(
                ClassLoaderUtils.getLoaderFile("simple-student.xml"),
                attrs,
                0,
                firstRow,
                0,
                ExcelTypeEnum.XLS,
                list
        );
        ExcelUtils.createExcel(workBook,
                new File("F:/test/excel/student-attr.xls"));
    }

    /**
     * 函数XML方式
     */
    @Test
    public void createExcelWithFunction() {
        List<Student> list = ObjectData.getStudentList(60000);
        Integer firstRow = 3;
        Function<Student, Object>[] functions = ExcelHelper.asArray(
                Student::getCode, Student::getName,
                (u) -> u.getSex().getName(),
                Student::getAge,
                (u) -> DateUtils.format(u.getBirthday(), DateConst.SIMPLE_DATE_PATTERN),
                (u) -> u.getFavourite().getValue());
        Workbook workBook = ExportBuilder.getWorkBook(
                ClassLoaderUtils.getLoaderFile("simple-student.xml"),
                functions,
                0,
                firstRow, 0,
                ExcelTypeEnum.XLS,
                list
        );
        ExcelUtils.createExcel(workBook,
                new File("F:/test/excel/student-attr.xls"));
    }

    @Test
    public void createExcelWithFunction1() {
        List<User> list = ObjectData.getUserList(100000);
        List<List<User>> lists = CollectionUtils.incise(list, 20000);
        IntFunction<List<User>[]> dataFunc = List[]::new;
        List<User>[] arrays = ArrayUtils.toArray(lists, dataFunc);
        Function<User, Object>[] functions = ArrayUtils.asArray(
                User::getCode, User::getName,
                (u) -> u.getSex().getName(),
                User::getAge,
                (u) -> DateUtils.format(u.getBirthday(), DateConst.SIMPLE_DATE_PATTERN),
                (u) -> u.getFavourite().getValue());
        Workbook workbook = ExportBuilder.getWorkBook(
                ClassLoaderUtils.getLoaderFile("simple-student.xml"),
                functions,
                0, 3, 0, ExcelTypeEnum.XLSX,
                arrays
        );
        ExcelUtils.createExcel(workbook, new File("F:/test/excel/function-user.xlsx"));
    }

    /**
     * 纯XML方式
     */
    @Test
    public void createExcelStudent() {
        List<Student> list = ObjectData.getStudentList(10000);
        Workbook workBook = ExportBuilder.getWorkBook(
//                ClassLoaderUtils.getLoaderFile("formula-student.xml"),
//                new File("F:/test/excel/formula-student.xlsx"),
                ClassLoaderUtils.getLoaderFile("simple-student.xml"),
                ExcelTypeEnum.XLS,
                ArrayUtils.asArray(list)
        );
        ExcelUtils.createExcel(workBook,
                new File("F:/test/excel/simple-student.xls"));
    }

    /**
     * 注解XML方式
     */
    @Test
    public void createExcelAnno() {
        List<User> list = ObjectData.getUserList(600);
        Workbook workBook = ExportBuilder.getWorkBook(
                ClassLoaderUtils.getLoaderFile("simple-user.xml"),
                ExcelTypeEnum.XLS,
                ArrayUtils.asArray(list)
        );
        boolean f = ExcelUtils.createExcel(workBook,
                new File("F:/test/excel/simple-user.xls"));
        System.out.println(f);
    }

    @Test
    public void start() {
        System.out.println(StringUtils.format("SUM(A%1$s:B%2$s:C%10$s:D%s:E%s)", 10));
        System.out.println(StringUtils.format("SUM(A${rownum}:D${rownum})", 20));
        System.out.println(StringUtils.format("AVG(A%1$s:B%2$s:D%s:E%s)", 20));
        System.out.println(StringUtils.format("IF(D%s>=18,D%s,'未成年人')", 20));
        System.out.println(StringUtils.format("SUM(A%s:D%s)+AVERAGE(A%s,D%s)", 20));
        System.out.println(String.format("sjdh%2$s", 10, 20));
    }

}