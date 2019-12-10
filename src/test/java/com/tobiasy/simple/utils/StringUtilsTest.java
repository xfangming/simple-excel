package com.tobiasy.simple.utils;

import com.tobiasy.simple.bean.SexEnum;
import com.tobiasy.simple.excel.ExcelHelper;
import org.junit.Test;

public class StringUtilsTest {
    @Test
    public void get(){
        SexEnum[] values = SexEnum.values();
        for (SexEnum value : values) {
            if (value.name().equals("MALE")) {
                System.out.println(value);
            }
        }
        Enum sex = EnumUtils.nameInstance(SexEnum::values, "MALE");
        System.out.println(sex);
        SexEnum sexEnum = (SexEnum) EnumUtils.nameInstance(SexEnum::values, ExcelHelper.parse("FEMALE", String.class));
        System.out.println(sexEnum);
    }
}