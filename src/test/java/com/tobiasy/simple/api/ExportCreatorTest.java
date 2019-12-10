package com.tobiasy.simple.api;

import com.tobiasy.simple.bean.ExcelFont;
import com.tobiasy.simple.bean.ObjectData;
import com.tobiasy.simple.bean.Student;
import com.tobiasy.simple.constants.DateConst;
import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.utils.ArrayUtils;
import com.tobiasy.simple.utils.ClassLoaderUtils;
import com.tobiasy.simple.utils.DateUtils;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.function.Function;

public class ExportCreatorTest {
    private String[] attrs = new String[]{"code", "name", "sex", "age", "birthday", "favourite"};
    private String[] titles = new String[]{"编码", "姓名", "性别", "年龄", "出生年月", "爱好"};
    private Function<Student, Object>[] functions = ArrayUtils.asArray(
            Student::getCode, Student::getName,
            (u) -> u.getSex().getName(),
            Student::getAge,
            (u) -> DateUtils.format(u.getBirthday(), DateConst.SIMPLE_DATE_PATTERN),
            (u) -> u.getFavourite().getValue());
    private List<Student> list = ObjectData.getStudentList(60000);

    /**
     * 纯模板方式
     */
    @Test
    public void buildTemplate() {
        new ExportCreator<Student>()
                .template("测试API模板", titles)
                .buildTemplate()
                .toExcel(new File("F:/test/excel/student-template.xls"));
    }

    @Test
    public void buildStyleTemplate() {
        new ExportCreator<Student>()
                .template(titles, new ExcelFont().setColor((short)20).setBold(true))
                .buildTemplate()
                .toExcel(new File("F:/test/excel/user-template1.xls"));
    }

    @Test
    public void buildTemplateXml() {
        new ExportCreator<Student>()
                .xmlFile(ClassLoaderUtils.getLoaderFile("simple-user.xml"))
//                .header(functions)
                .type(ExcelTypeEnum.XLSX)
                .build().
                toExcel(new File("F:/test/excel/student-api.xls"));
    }

    /**
     * 属性模板方式
     */
    @Test
    public void buildAttrTemplate() {
        new ExportCreator<Student>()
                .template("测试API模板", titles)
//                .template(titles, new ExcelFont().setColor((short)20).setBold(true))//仅支持模板，还不支持数据导出
                .header(attrs)
                .body(list)
                .buildTemplate()
                .toExcel(new File("F:/test/excel/student-template.xls"));
    }

    /**
     * 属性XML方式
     */
    @Test
    public void buildWithXmlAttr() {
        List<Student> list = ObjectData.getStudentList(100);
        new ExportCreator<Student>()
                .xmlFile(ClassLoaderUtils.getLoaderFile("simple-student.xml"))
                .header(attrs)
                .body(list, Position.of(0, 5, 0))
                .type(ExcelTypeEnum.XLSX)
                .build().
                toExcel(new File("F:/test/excel/student-api.xls"));
    }

    /**
     * 函数XML方式
     */
    @Test
    public void buildWithXmlFunction() {
        List<Student> list = ObjectData.getStudentList(60);
        new ExportCreator<Student>()
                .xmlFile(ClassLoaderUtils.getLoaderFile("simple-student.xml"))
                .header(functions)
                .body(list, Position.of(0, 10, 0))
                .type(ExcelTypeEnum.XLSX)
                .buildXml().
                toExcel(new File("F:/test/excel/student-api.xls"));
    }

    /**
     * 纯注解方式
     */
    @Test
    public void buildOnlyAnnotation() {
        new ExportCreator<Student>()
                .body(list)
                .buildOnlyAnnotation()
                .toExcel(new File("F:/test/excel/student-template.xls"));
    }

    /**
     * 注解XML方式
     */
    @Test
    public void buildByAnnotation() {
        new ExportCreator<Student>()
                .xmlFile(ClassLoaderUtils.getLoaderFile("simple-student.xml"))
                .body(list)
                .type(ExcelTypeEnum.XLSX)
                .buildXmlAnnotation()
                .toExcel(new File("F:/test/excel/student-anno.xls"));
    }
}