package com.tobiasy.simple.excel;

import com.tobiasy.simple.bean.GenericType;
import com.tobiasy.simple.bean.Student;
import com.tobiasy.simple.bean.User;
import com.tobiasy.simple.utils.ArrayUtils;
import com.tobiasy.simple.utils.ClassHelper;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ExcelFieldScannerTest {

    @Test
    public void main() {
        String[] excelField = ExcelAnnotation.getOrderFields(Student.class);
        ArrayUtils.print(excelField);
        Set<Student> list = new HashSet<>();
        list.add(null);
        list.add(new Student());
        System.out.println(ClassHelper.getClass(list));
        Class<Student> classType = new GenericType<Student>() {
        }.getClassType();
        System.out.println(classType);
//        System.out.println(ClassHelper.getGenericSuperclass(list));
    }

    @Test
    public void getFunction() {
        Function<User, String> getName = User::getName;
        getName.apply(new User());
    }
}