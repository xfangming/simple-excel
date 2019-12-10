package com.tobiasy.simple.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author tobiasy
 * @date 2019/7/17
 */
public class ObjectData {
    public static List<Student> getStudentList(int size) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Student student = new Student();
            student.setCode(i);
            student.setName("admin" + i);
            student.setAge(3 + i);
            student.setBirthday(new Date());
            if (i % 2 == 0) {
                student.setSex(SexEnum.FEMALE);
                student.setFavourite(Favourite.BASKETBALL);
            } else {
                if (i % 3 == 0) {
                    student.setFavourite(Favourite.PINGPANG);
                } else {
                    student.setFavourite(Favourite.FOOTBALL);
                }
                student.setSex(SexEnum.MALE);
            }
            student.setOther("other"+i);
            student.setPrice(new BigDecimal("100."+i));
            student.setHeight(new Double("120."+i));
            if (i%2 == 0) {
                student.setWork(Boolean.TRUE);
            } else {
                student.setWork(Boolean.FALSE);
            }
            students.add(student);
        }
        return students;
    }
    public static List<User> getUserList(int size) {
        List<User> students = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            User student = new User();
            student.setCode(i);
            student.setName("admin" + i);
            student.setAge(18 + i);
            student.setBirthday(new Date());
            if (i % 2 == 0) {
                student.setSex(SexEnum.MALE);
                student.setFavourite(Favourite.BASKETBALL);
            } else {
                if (i % 3 == 0) {
                    student.setFavourite(Favourite.PINGPANG);
                } else {
                    student.setFavourite(Favourite.FOOTBALL);
                }
                student.setSex(SexEnum.FEMALE);
            }
            student.setOther("other"+i);
            student.setPrice(new BigDecimal("100."+i));
            student.setHeight(new Double("120."+i));
            if (i%2 == 0) {
                student.setWork(Boolean.TRUE);
            } else {
                student.setWork(Boolean.FALSE);
            }
            students.add(student);
        }
        return students;
    }
}
