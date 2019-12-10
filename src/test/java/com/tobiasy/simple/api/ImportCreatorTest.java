package com.tobiasy.simple.api;

import com.tobiasy.simple.bean.Favourite;
import com.tobiasy.simple.bean.SexEnum;
import com.tobiasy.simple.bean.Student;
import com.tobiasy.simple.constants.DateConst;
import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.excel.ExcelHelper;
import com.tobiasy.simple.utils.ArrayUtils;
import com.tobiasy.simple.utils.DateUtils;
import com.tobiasy.simple.utils.EnumUtils;
import org.junit.Test;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.tobiasy.simple.excel.ExcelHelper.*;
import static com.tobiasy.simple.function.ExcelFunction.TO_STRING;


public class ImportCreatorTest {
    private String[] attrs = new String[]{"code", "name", "sex", "age", "birthday", "favourite"};
    private Function<String, Object>[] functions = ExcelHelper.asArray(
            TO_INTEGER, TO_STRING,
            toEnumName(SexEnum.class),
            TO_INTEGER,
            (s) -> TO_DATE_FORMAT.apply(s, DateConst.SIMPLE_DATE_PATTERN),
            toEnumValue(Favourite.class)
    );

    private BiConsumer<Student, String>[] setters = ArrayUtils.asArray(
            (t, o) -> t.setCode(ExcelHelper.parse(o, Integer.class)),
            (t, o) -> t.setName(ExcelHelper.parse(o, String.class)),
            (t, o) -> t.setSex((SexEnum) EnumUtils.nameInstance(SexEnum::values, ExcelHelper.parse(o, String.class))),
            (t, o) -> t.setAge(ExcelHelper.parse(o, Integer.class)),
            (t, o) -> t.setBirthday(DateUtils.parse(o, DateConst.SIMPLE_DATE_PATTERN)),
            (t, o) -> t.setFavourite(Favourite.getInstance(ExcelHelper.parse(o, String.class)))
    );

    /**
     * 字符函数方式
     */
    @Test
    public void toList() {
        File file = new File("F:/test/excel/student-api.xls");
        new ImportCreator<Student>()
                .header(attrs)
                .body(functions, Position.of(0, 10, 0))
                .inputFile(file).type(ExcelTypeEnum.XLSX)
                .buildAnnotation(Student::new)
                .toList()
                .forEach(System.out::println);
    }

    /**
     * setter函数方式
     */
    @Test
    public void toSimpleList() {
        File file = new File("F:/test/excel/student-api.xls");
        new ImportCreator<Student>()
                .header(setters)
                .body(Position.of(0, 3, 0))
                .inputFile(file).type(ExcelTypeEnum.XLSX)
                .build(Student::new)
                .toList()
                .forEach(System.out::println);
    }

    /**
     * 纯注解方式
     */
    @Test
    public void buildAnnotation() {
        File file = new File("F:/test/excel/student-api.xls");
        new ImportCreator<Student>()
                .inputFile(file)
                .type(ExcelTypeEnum.XLSX)
                .buildAnnotation(Student::new)
                .toList()
                .forEach(System.out::println);
    }
}