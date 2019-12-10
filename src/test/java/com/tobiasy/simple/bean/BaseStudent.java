package com.tobiasy.simple.bean;

import com.tobiasy.simple.anno.ExcelField;

import java.util.Date;

/**
 * @author tobiasy
 * @date 2019/8/12
 */
public class BaseStudent {
    @ExcelField(index = 3, function = "TO_INTEGER", columnSize = 1)
    private int age;
    @ExcelField(index = 2, function = "TO_SEX", columnSize = 1)
    private SexEnum sex;
    @ExcelField(index = 4, function = "TO_DATE", columnSize = 1)
    private Date birthday;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
