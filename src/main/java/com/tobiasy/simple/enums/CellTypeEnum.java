package com.tobiasy.simple.enums;

/**
 * excel中单元格类型对应
 *
 * @author tobiasy
 * @date 2018/12/12
 */
public enum CellTypeEnum {
    /**
     * 数字类型，包括整形
     */
    NUMERIC("numeric", "Integer,int,short,byte,Byte"),

    /**
     * 浮点型
     */
    DOUBLE("double", "BigDecimal,Double,double,float"),

    /**
     * 字符串
     */
    STRING("string", "String"),

    /**
     * 时间类型
     */
    DATE("date", "Date"),

    BOOLEAN("boolean", "Boolean,boolean"),

    /**
     * 枚举类型
     */
    ENUM("enum", "Enum");
    private String key;
    private String javaType;

    CellTypeEnum(String key, String javaType) {
        this.key = key;
        this.javaType = javaType;
    }

    public static CellTypeEnum getInstance(Class javaType) {
        String type = javaType.getSimpleName();
        CellTypeEnum[] enums = CellTypeEnum.values();
        for (CellTypeEnum e : enums) {
            String javaTypes = e.getJavaType();
            String[] types = javaTypes.split(",");
            for (String tp : types) {
                if (tp.equals(type)) {
                    return e;
                }
            }
        }
        Class superclass = javaType.getSuperclass();
        if (superclass != null) {
            return getInstance(superclass);
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
}