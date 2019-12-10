package com.tobiasy.simple.api;

import com.tobiasy.simple.bean.ExcelFont;

/**
 * @author tobiasy
 * @date 2019/11/19
 */
public class ExportTemplate {
    private String[] titles;
    private ExcelFont excelFont;
    private String head;
    private boolean hasFont;

    public ExportTemplate(String head, String[] titles) {
        this.head = head;
        this.titles = titles;
        this.hasFont = false;
    }

    public ExportTemplate(String[] titles, ExcelFont excelFont) {
        this.titles = titles;
        this.excelFont = excelFont;
        this.hasFont = true;
    }

    public static ExportTemplate of(String head, String[] titles) {
        return new ExportTemplate(head, titles);
    }

    public static ExportTemplate of(String[] titles, ExcelFont excelFont) {
        return new ExportTemplate(titles, excelFont);
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] headers) {
        this.titles = headers;
    }

    public ExcelFont getExcelFont() {
        return excelFont;
    }

    public void setExcelFont(ExcelFont excelFont) {
        this.excelFont = excelFont;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public boolean isHasFont() {
        return hasFont;
    }

    public void setHasFont(boolean hasFont) {
        this.hasFont = hasFont;
    }
}
