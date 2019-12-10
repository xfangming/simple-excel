package com.tobiasy.simple.excel;

import com.tobiasy.simple.bean.ColGroup;
import com.tobiasy.simple.constants.ExcelConstants;
import com.tobiasy.simple.enums.BooleanEnum;
import com.tobiasy.simple.utils.ReflectUtils;
import com.tobiasy.simple.utils.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tobiasy
 * @date 2019/8/6
 */
public class ConfigHandler {

    /**
     * 样式操作
     *
     * @param wb Workbook
     * @param tr 标签样式
     * @return
     */
    public static CellStyle getCellStyle(Workbook wb, Element tr) {
        CellStyle cellStyle = wb.createCellStyle();
        if (tr == null) {
            return cellStyle;
        }
        Font font = wb.createFont();
        //设置字体
        Attribute isBold = tr.getAttribute("isbold");
        Attribute fontSize = tr.getAttribute("fontsize");
        Attribute color = tr.getAttribute("color");
        Attribute fontName = tr.getAttribute("fontname");
        if (fontName != null) {
            font.setFontName(fontName.getValue());
        }
        if (getBooleanAttribute(isBold)) {
            font.setBold(true);
        } else {
            font.setBold(false);
        }
        if (fontSize != null) {
            font.setFontHeightInPoints(Short.parseShort(
                    fontSize.getValue().replaceAll("[a-z,A-Z,\\.]", "")));
        }
        if (color != null) {
            font.setColor(Short.parseShort(color.getValue()));
        }
        cellStyle.setFont(font);
        String align = getAttribute(tr, "align", null);
        String verticalAlign = getAttribute(tr, "verticalalign", null);
        String allalign = getAttribute(tr, "allalign", null);
        if (align != null) {
            cellStyle.setAlignment(HorizontalAlignment.valueOf(align.toUpperCase()));
        } else if (allalign != null) {
            cellStyle.setAlignment(HorizontalAlignment.valueOf(allalign.toUpperCase()));
        }
        if (verticalAlign != null) {
            cellStyle.setVerticalAlignment(VerticalAlignment.valueOf(verticalAlign.toUpperCase()));
        } else if (allalign != null) {
            cellStyle.setVerticalAlignment(VerticalAlignment.valueOf(allalign.toUpperCase()));
        }

        String borderAll = getAttribute(tr, "borderall", null);
        String borderTop = getAttribute(tr, "bordertop", null);
        cellStyleInvoke(cellStyle, "setBorderTop", borderTop, borderAll);
        String borderRight = getAttribute(tr, "borderright", null);
        cellStyleInvoke(cellStyle, "setBorderRight", borderRight, borderAll);
        String borderBottom = getAttribute(tr, "borderbottom", null);
        cellStyleInvoke(cellStyle, "setBorderBottom", borderBottom, borderAll);
        String borderLeft = getAttribute(tr, "borderleft", null);
        cellStyleInvoke(cellStyle, "setBorderLeft", borderLeft, borderAll);
        return cellStyle;
    }

    private static void cellStyleInvoke(CellStyle cellStyle, String method, String first, String second) {
        if (first != null) {
            ReflectUtils.invoke(cellStyle, method, BorderStyle.class, BorderStyle.valueOf(first.toUpperCase()));
        } else {
            if (second != null) {
                ReflectUtils.invoke(cellStyle, method, BorderStyle.class, BorderStyle.valueOf(second.toUpperCase()));
            }
        }
    }

    /**
     * 在指定元素Element中获取属性名为attribute的值，没有该属性则返回默认值defaultValue
     *
     * @param element      指定元素
     * @param attribute    属性名
     * @param defaultValue 默认值
     * @return
     */
    protected static int getIntAttribute(Element element, String attribute, Integer defaultValue) {
        if (element == null) {
            return defaultValue;
        }
        Attribute attr = element.getAttribute(attribute);
        int value = 0;
        try {
            value = attr != null ? attr.getIntValue() : defaultValue;
        } catch (DataConversionException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static String getAttribute(Element element, String attribute, String defaultValue) {
        Attribute attr = element.getAttribute(attribute);
        String value;
        value = attr != null ? attr.getValue() : defaultValue;
        return value;
    }

    /**
     * 设置列宽
     *
     * @param colGroup
     */
    protected static int setColumnWidth(Sheet sheet, Element colGroup) {
        List cols = colGroup.getChildren("col");
        for (Object object : cols) {
            ColGroup group = getColGroup(object);
            Integer width = group.getWidth();
            if (width != null && width != 0) {
                sheet.setColumnWidth(group.getIndex(), width);
            }
        }
        return cols.size();
    }

    private static ColGroup getColGroup(Object object) {
        Element col = (Element) object;
        Attribute widthAtt = col.getAttribute("width");
        Attribute indexAtt = col.getAttribute("index");
        int index = CellUtils.getIndex(indexAtt.getValue());
        Integer width = getNumberOutUnit(widthAtt);
        return new ColGroup(index, width);
    }

    protected static List<ColGroup> getColumnWidth(Element colGroup) {
        List cols = colGroup.getChildren("col");
        List<ColGroup> colGroups = new ArrayList<>();
        for (Object object : cols) {
            colGroups.add(getColGroup(object));
        }
        return colGroups;
    }

    /**
     * 获取除单位以外的数值
     *
     * @param param 节点元素属性对象
     * @return
     */
    public static Integer getNumberOutUnit(Attribute param) {
        if (param == null) {
            return null;
        }
        String widthValue = param.getValue();
        if ("".equals(widthValue)) {
            return 0;
        }
        String unit = widthValue.replaceAll("[0-9,\\.]", "");
        String value = widthValue.replaceAll(unit, "");
        int v = 60;
        if (StringUtils.isBlank(unit) || ExcelConstants.UNIT_PX.endsWith(unit)) {
            v = Math.round(Float.parseFloat(value) * 37F);
        } else if (ExcelConstants.UNIT_EM.endsWith(unit)) {
            v = Math.round(Float.parseFloat(value) * 267.5F);
        }
        return v;
    }

    private static boolean getBooleanAttribute(Attribute attribute) {
        return getBooleanAttribute(attribute, false);
    }

    private static boolean getBooleanAttribute(Attribute attribute, Boolean isNullReturn) {
        if (attribute == null || attribute.getValue() == null) {
            return isNullReturn;
        }
        return BooleanEnum.TRUE.getKey().equals(attribute.getValue());
    }
}
