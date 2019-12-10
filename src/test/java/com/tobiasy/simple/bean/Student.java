package com.tobiasy.simple.bean;

import com.tobiasy.simple.anno.ExcelField;
import com.tobiasy.simple.anno.ExportUseAnnotation;
import com.tobiasy.simple.anno.FunctionTarget;
import com.tobiasy.simple.anno.ImportUseAnnotation;

import java.math.BigDecimal;
import java.util.Date;

@ExportUseAnnotation(name = "用户信息表", firstRow = 3)
@FunctionTarget(value = SubExcelFunction.class)
@ImportUseAnnotation(firstRow = 9)
public class Student {
    /**
     * 编号 	姓名	年龄	性别	出生日期	 爱好
     */
    private Integer id;
    @ExcelField(name = "编号", function = "TO_INTEGER")
    private Integer code;
    @ExcelField(name = "姓名", index = 1)
    private String name;
    @ExcelField(name = "年龄", index = 3, function = "TO_INTEGER", columnSize = 1)
    private int age;
    @ExcelField(name = "性别", index = 2, function = "TO_SEX", columnSize = 1)
    private SexEnum sex;
    @ExcelField(name = "出生日期", index = 4, columnSize = 1)//dateFormat = "yyyy-MM-dd HH:mm:ss",
    private Date birthday;
    @ExcelField(name = "爱好", index = 5, function = "TO_FAVOURITE")
    private Favourite favourite;
//    @ExcelField(8)
    private String other;
//    @ExcelField(index = 9, function = "TO_BIGDECIMAL")
    private BigDecimal price;
//    @ExcelField(index = 10, function = "TO_DOUBLE")
    private Double height;
//    @ExcelField(index = 11, function = "TO_BOOLEAN")
    private Boolean work;
    private String test;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Favourite getFavourite() {
        return favourite;
    }

    public void setFavourite(Favourite favourite) {
        this.favourite = favourite;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Boolean getWork() {
        return work;
    }

    public void setWork(Boolean work) {
        this.work = work;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "Student{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", favourite='" + favourite + '\'' +
                ", other='" + other + '\'' +
                ", price='" + price + '\'' +
                ", height='" + height + '\'' +
                ", work='" + work + '\'' +
                '}';
    }
}