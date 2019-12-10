package com.tobiasy.simple.bean;

/**
 * @author tobiasy
 * @date 2018/11/9
 */
public class ExcelFont {
    private Boolean bold;
    private String fontName;
    private Short fontHeightInPoints;
    private Short color;

    public Boolean getBold() {
        return bold;
    }

    public ExcelFont setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public String getFontName() {
        return fontName;
    }

    public ExcelFont setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    public Short getFontHeightInPoints() {
        return fontHeightInPoints;
    }

    public ExcelFont setFontHeightInPoints(Short fontHeightInPoints) {
        this.fontHeightInPoints = fontHeightInPoints;
        return this;
    }

    public Short getColor() {
        return color;
    }

    public ExcelFont setColor(Short color) {
        this.color = color;
        return this;
    }
}