package com.tobiasy.simple.enums;

/**
 * @author tobiasy
 * @date 2019/7/16
 */
public enum ExcelTypeEnum {
    /**
     * Excel文件类型
     */
    XLS(".xls"),
    XLSX(".xlsx");

    ExcelTypeEnum(String suffix) {
        this.suffix = suffix;
    }

    private String suffix;

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
