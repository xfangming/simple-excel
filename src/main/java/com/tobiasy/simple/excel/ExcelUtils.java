package com.tobiasy.simple.excel;

import com.tobiasy.simple.exception.OperationException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;

/**
 * @author tobiasy
 * @date 2018/10/9
 */
public class ExcelUtils {

    /**
     * 数据写入磁盘文件
     *
     * @param wb
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void write(Workbook wb, File file) throws IOException {
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fout = new FileOutputStream(file);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        wb.write(output);
        fout.write(output.toByteArray());
        fout.flush();
        output.close();
        fout.close();
    }

    public static boolean createExcel(Workbook workbook, File file) {
        try {
            if (file == null) {
                throw new OperationException("parameter file can not be null!");
            }
            if (file.exists()) {
                if (!file.delete()) {
                    throw new OperationException("file %s can not delete!", file.getName());
                }
            }
            if (!file.createNewFile()) {
                throw new OperationException("file %s can not create!", file.getName());
            }
            FileOutputStream stream = FileUtils.openOutputStream(file);
            if (workbook != null) {
                workbook.write(stream);
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
