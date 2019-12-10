package com.tobiasy.simple.excel;

import com.tobiasy.simple.utils.ClassLoaderUtils;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author tobiasy
 * @date 2018/10/18
 */
public class DownloadUtil {
    /**
     * @param header   标题
     * @param fileName 文件名称，有后缀
     */
    public static void export(HttpServletResponse response, String[] header, String fileName) {
        export(response, TemplateBuilder.getWorkbook(header), fileName);
    }

    public static void export(HttpServletResponse response, Workbook workbook, String fileName) {
        try {
            OutputStream out = response.getOutputStream();
            response.reset();
            response.setContentType("application/msexcel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" +
                    java.net.URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean toResponse(HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            OutputStream out = response.getOutputStream();
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean download(HttpServletResponse response, String fileName) {
        try {
            File file = ClassLoaderUtils.getLoaderFile(fileName);
            if (!file.exists()) {
                throw new RuntimeException("没有找到下载模板！");
            }
            response.setHeader("Content-disposition", "attachment; filename=" +
                    java.net.URLEncoder.encode(file.getName(), "UTF-8"));
            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(file);
            response.setContentType("application/octet-stream;charset=UTF-8");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     * response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "UTF-8"));
     */
}