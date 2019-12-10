package com.tobiasy.simple.bean;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

/**
 * @author tobiasy
 * @date 2019/7/24
 */
public class WorkbookResult {
    private Workbook workbook;
    private Map<Integer, SheetResult> sheetMap;
    private List<ColGroup> colGroups;

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public Map<Integer, SheetResult> getSheetMap() {
        return sheetMap;
    }

    public void setSheetMap(Map<Integer, SheetResult> sheetMap) {
        this.sheetMap = sheetMap;
    }

    public List<ColGroup> getColGroups() {
        return colGroups;
    }

    public void setColGroups(List<ColGroup> colGroups) {
        this.colGroups = colGroups;
    }
}
