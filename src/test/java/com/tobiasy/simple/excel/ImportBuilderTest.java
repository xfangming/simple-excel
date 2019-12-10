package com.tobiasy.simple.excel;

import com.tobiasy.simple.bean.*;
import com.tobiasy.simple.constants.DateConst;
import com.tobiasy.simple.enums.ExcelTypeEnum;
import com.tobiasy.simple.utils.ArrayUtils;
import com.tobiasy.simple.utils.DateUtils;
import com.tobiasy.simple.utils.ReflectUtils;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.tobiasy.simple.excel.ExcelHelper.*;


public class ImportBuilderTest {

    /**
     * excel导入，导入数据为 Map 集合
     */
    @Test
    public void getDataTest() {
        File file = new File("F:/test/excel/simple-student.xlsx");
        Integer firstRow = 3;
        List<Map<Integer, String>> list = ImportBuilder.getData(file, firstRow);
        System.out.println(list.size());
        list.forEach(System.out::println);
    }

    /**
     * 字符函数方式
     */
    @Test
    public void getListTest() throws FileNotFoundException {
        File file = new File("F:/test/excel/simple-student.xls");
        FileInputStream inputStream = new FileInputStream(file);
        Integer firstRow = 4;
        String[] attrs = new String[]{"code", "name", "sex", "age", "birthday", "favourite"};
        Function<String, Object>[] functions = ArrayUtils.asArray(
                TO_INTEGER,
                (s) -> s,
                (s) -> TO_ENUM_NAME.apply(s, SexEnum.class),
                TO_INTEGER,
                TO_DATE,
                (s) -> TO_ENUM_VALUE.apply(s, Favourite.class));
        List<Student> list = ImportBuilder.getList(
                inputStream, 0, firstRow,
                attrs, functions,
                Student::new, ExcelTypeEnum.XLSX);
        System.out.println(list.size());
        list.forEach(System.out::println);
    }

    @Test
    public void getListTest1() {
        //ExcelHelper helper = ExcelHelper.getInstance(Student.class);
        File file = new File("F:/test/excel/simple-student.xls");
        Integer firstRow = 10;
        String[] attrs = new String[]{"code", "name", "sex", "age", "birthday", "favourite"};
        Function<String, Object>[] functions = ExcelHelper.asArray(
                TO_INTEGER, TO_STRING, toEnumName(SexEnum.class), TO_INTEGER, TO_DATE, toEnumValue(Favourite.class)
        );
        List<Student> list = ImportBuilder.getList(
                file, 0, firstRow,
                attrs, functions,
                Student::new, ExcelTypeEnum.XLSX);
        System.out.println(list.size());
        list.forEach(System.out::println);
    }

    /**
     * setter函数方式
     */
    @Test
    public void getListBySetter() {
        File file = new File("F:/test/excel/simple-student.xls");
        InputStream inputStream = ExcelHelper.TO_INPUT_STREAM.apply(file);
        Integer firstRow = 4;
        BiConsumer<Student, String>[] setters = ArrayUtils.asArray(
                (t, o) -> t.setCode(ExcelHelper.parse(o, Integer.class)),
                (t, o) -> t.setName(ExcelHelper.parse(o, String.class)),
                (t, o) -> t.setSex(SexEnum.getInstance(ExcelHelper.parse(o, String.class))),
                (t, o) -> t.setAge(ExcelHelper.parse(o, Integer.class)),
                (t, o) -> t.setBirthday(DateUtils.parse(o, DateConst.SIMPLE_DATE_PATTERN)),
                (t, o) -> t.setFavourite(Favourite.getInstance(ExcelHelper.parse(o, String.class)))
        );
        List<Student> list = ImportBuilder.getList(
                inputStream, 0, firstRow,
                setters,
                Student::new, ExcelTypeEnum.XLSX);
        System.out.println(list.size());
        list.forEach(System.out::println);
    }

    /**
     * 纯注解方式
     */
    @Test
    public void getListByAnnotation() {
        File file = new File("F:/test/excel/simple-student.xls");
        //Integer firstRow = 4;
        List<Student> result = ImportBuilder.getList(
                ExcelHelper.TO_INPUT_STREAM.apply(file),
                Student::new,
                ExcelTypeEnum.XLSX);
        System.out.println(result.size());
        result.forEach(System.out::println);
    }

    @Test
    public void getUserListByAnnotation() {
        File file = new File("F:/test/excel/simple-user.xls");
        //Integer firstRow = 10;
        List<User> result = ImportBuilder.getList(ExcelHelper.TO_INPUT_STREAM.apply(file), User::new, ExcelTypeEnum.XLS);
        System.out.println(result.size());
        result.forEach(System.out::println);
    }

    @Test
    public void getFunction() {
        List<Field> globalFields = ReflectUtils.getGlobalFields(SubExcelFunction.class);
        globalFields.forEach(System.out::println);
    }

    private BiConsumer<Student, String>[] getBiConsumer() {
        return ArrayUtils.asArray(
                (t, o) -> t.setCode(ExcelHelper.parse(o, Integer.class)),
                (t, o) -> t.setName(ExcelHelper.parse(o, String.class)),
                (t, o) -> t.setSex(SexEnum.getInstance(ExcelHelper.parse(o, String.class))),
                (t, o) -> t.setAge(ExcelHelper.parse(o, Integer.class)),
                (t, o) -> t.setBirthday(DateUtils.parse(o, DateConst.SIMPLE_DATE_PATTERN)),
                (t, o) -> t.setFavourite(Favourite.getInstance(ExcelHelper.parse(o, String.class)))
        );
    }
}