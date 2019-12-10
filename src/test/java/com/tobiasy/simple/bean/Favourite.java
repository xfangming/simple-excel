package com.tobiasy.simple.bean;

public enum Favourite {
    FOOTBALL(1, "足球"),
    BASKETBALL(2, "篮球"),
    PINGPANG(3, "乒乓球");
    private Integer code;
    private String value;

    Favourite(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static Favourite getInstance(String name){
        for (Favourite favourite : Favourite.values()) {
            String name1 = favourite.getValue();
            if (name1.equals(name)) {
                return favourite;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}