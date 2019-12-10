package com.tobiasy.simple.enums;

/**
 * @author tobiasy
 * @date 2018/10/15
 */
public enum BooleanEnum {
    /**
     * 布尔类型中的 true
     */
    TRUE("true", true),

    /**
     * 布尔类型中的 false
     */
    FALSE("false", false);
    private String key;
    private Boolean value;

    BooleanEnum(String key, Boolean value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据字符串初始化布尔类型
     *
     * @param key 布尔类型字符串
     * @return 布尔类型
     * @throws RuntimeException 运行期异常
     */
    public static Boolean getInstance(String key) throws RuntimeException {
        BooleanEnum[] enums = BooleanEnum.values();
        for (BooleanEnum e : enums) {
            if (e.getKey().equalsIgnoreCase(key)) {
                return e.getValue();
            }
        }
        throw new RuntimeException(key + " can not cast to Boolean");
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}