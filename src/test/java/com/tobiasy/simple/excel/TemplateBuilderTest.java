package com.tobiasy.simple.excel;

import com.tobiasy.simple.bean.ObjectData;
import com.tobiasy.simple.bean.Student;
import com.tobiasy.simple.constants.DateConst;
import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.utils.DateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.function.Function;

/**
 * @author tobiasy
 * @date 2019/7/17
 */
public class TemplateBuilderTest {
    private String[] header = {"分类名称", "分类代码", "预算金额", "已冻结金额", "已使用金额", "剩余金额"};
    private String[] titles = {"编码", "名称", "性别", "年龄", "出生年月", "爱好"};

    @Test
    public void createExcel() {
        File file = new File("F:/test/excel-test.xls");
        Workbook workbook = TemplateBuilder.getWorkbook(header, null);
        ExcelUtils.createExcel(workbook, file);
    }

    @Test
    public void createExcel2() {
        File file = new File("F:/test/excel-test2.xls");
        Workbook workbook = TemplateBuilder.getWorkbook("用户信息表", header);
        ExcelUtils.createExcel(workbook, file);
    }

    /**
     * 函数模板方式
     */
    @Test
    public void createExcelByTemplate() {
        List<Student> studentList = ObjectData.getStudentList(60000);
        String[] header = {"编码", "名称", "性别", "年龄", "出生年月", "爱好"};
        Function<Student, Object>[] attrs = ExcelHelper.asArray(
                Student::getCode,
                Student::getName,
                (s) -> s.getSex().getName(),
                Student::getAge,
                (s) -> DateUtils.format(s.getBirthday(), DateConst.SIMPLE_DATE_PATTERN),
                (s) -> s.getFavourite().getValue());
        File file = new File("F:/test/excel/excel-test3.xls");
        Workbook workbook = TemplateBuilder.getWorkbook("用户信息表", titles,
                attrs,
                studentList,
                ExcelTypeEnum.XLS);
        ExcelUtils.createExcel(workbook, file);
    }

    /**
     * 注解模板方式
     */
    @Test
    public void createExcelByAnno() {
        List<Student> studentList = ObjectData.getStudentList(60000);
        Workbook workbook = TemplateBuilder.getWorkbook(studentList, ExcelTypeEnum.XLSX);
        ExcelUtils.createExcel(workbook, new File("F:/test/excel/excel-anno.xls"));
    }


}