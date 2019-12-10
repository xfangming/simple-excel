package com.tobiasy.simple.bean;

/**
 * @author tobiasy
 * @date 2019/5/5
 */
public enum SexEnum {
    MALE(1, "男"),
    FEMALE(2, "女"),
    OTHER(3, "其他");
    SexEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }
    public static SexEnum getInstance(String name){
        SexEnum[] values = SexEnum.values();
        for (SexEnum sexEnum : values) {
            if (sexEnum.getName().equals(name)) {
                return sexEnum;
            }
        }
        return null;
    }
    private Integer code;
    private String name;

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
}
