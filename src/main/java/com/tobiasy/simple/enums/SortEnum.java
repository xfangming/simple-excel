package com.tobiasy.simple.enums;

/**
 * @author tobiasy
 * 排序
 */
public enum SortEnum {
    /**
     * 升序
     */
    ASC(1, "asc"),

    /**
     * 降序
     */
    DESC(2, "desc"),;

    SortEnum(Integer code, String discription) {
        this.code = code;
        this.discription = discription;
    }

    private Integer code;
    private String discription;

    public Integer getCode() {
        return code;
    }

    public String getDiscription() {
        return discription;
    }
}
