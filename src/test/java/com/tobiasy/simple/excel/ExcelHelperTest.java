package com.tobiasy.simple.excel;

import com.tobiasy.simple.bean.Student;
import com.tobiasy.simple.bean.SubExcelFunction;
import com.tobiasy.simple.function.ExcelFunction;
import org.junit.Test;

import java.util.function.Function;

public class ExcelHelperTest {

    @Test
    public void toDate() {
        ExcelHelper instance = ExcelHelper.getInstance(Student.class);
        Function<String, Object> toDate = ExcelHelper.TO_DATE;
        System.out.println(toDate.apply("2019-10-29 13:40:20"));
        ExcelFunction excelFunction = new ExcelFunction();
        System.out.println(excelFunction.getDefaultPattern());
        excelFunction = new SubExcelFunction();
        System.out.println(excelFunction.getDefaultPattern());
    }

}