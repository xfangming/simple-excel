package com.tobiasy.simple.bean;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdom.Element;

import java.util.List;

/**
 * @author tobiasy
 */
public class ExcelResult {
    private HSSFWorkbook workbook;
    private Element tr;
    private List<Element> elements;
    private Integer rownum;
    private String templateName;

    public HSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public Element getTr() {
        return tr;
    }

    public void setTr(Element tr) {
        this.tr = tr;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public Integer getRownum() {
        return rownum;
    }

    public void setRownum(Integer rownum) {
        this.rownum = rownum;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
