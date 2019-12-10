package com.tobiasy.simple.excel;

import com.tobiasy.simple.bean.Student;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExcelAnnoUtilsTest {

    @Test
    public void getExcelFunctions() {
        System.out.println(ExcelAnnotation.getFunctionTarget(Student.class));
    }
}