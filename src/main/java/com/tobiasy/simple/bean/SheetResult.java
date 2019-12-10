package com.tobiasy.simple.bean;

import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;

/**
 * @author tobiasy
 * @date 2019/7/24
 */
public class SheetResult {
    private Short lineHeight;
    private Integer startRow;
    private List<OrderField> orderField;
    private CellStyle cellStyle;
    private Integer sheetAt;
    private List<ColGroup> colGroups;

    public Short getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(Short lineHeight) {
        this.lineHeight = lineHeight;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public List<OrderField> getOrderField() {
        return orderField;
    }

    public void setOrderField(List<OrderField> orderField) {
        this.orderField = orderField;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public Integer getSheetAt() {
        return sheetAt;
    }

    public void setSheetAt(Integer sheetAt) {
        this.sheetAt = sheetAt;
    }

    public List<ColGroup> getColGroups() {
        return colGroups;
    }

    public void setColGroups(List<ColGroup> colGroups) {
        this.colGroups = colGroups;
    }
}
