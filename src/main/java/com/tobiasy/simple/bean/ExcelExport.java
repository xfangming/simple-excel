package com.tobiasy.simple.bean;

import com.tobiasy.simple.enums.ExcelTypeEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @author tobiasy
 * @date 2019/10/22
 */
public class ExcelExport<T> {
    private File xmlFile;
    private HttpServletResponse response;
    private String fileName;
    private List<T> lists;
    private ExcelTypeEnum typeEnum;
    private File tempFile;
}
