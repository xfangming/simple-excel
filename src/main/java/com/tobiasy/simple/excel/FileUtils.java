package com.tobiasy.simple.excel;

import com.tobiasy.simple.exception.OperationException;

import java.io.*;

/**
 * @author tobiasy
 * @date 2019/7/17
 */
public class FileUtils {

    public static FileOutputStream openOutputStream(File file) {
        try {
            return openOutputStream(file, false);
        } catch (IOException e) {
            e.printStackTrace();
            throw new OperationException(e.getMessage());
        }
    }

    public static InputStream getInputStream(File input) {
        try {
            return new FileInputStream(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File \'" + file + "\' exists but is a directory");
            }

            if (!file.canWrite()) {
                throw new IOException("File \'" + file + "\' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
                throw new IOException("Directory \'" + parent + "\' could not be created");
            }
        }

        return new FileOutputStream(file, append);
    }

    public static File getFile(String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (newFile) {
                    return file;
                } else {
                    return null;
                }
            } else {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File getFileWithDirs(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file;
            }
            File parentFile = file.getParentFile();
            if (parentFile.exists()) {
                return getFile(filePath);
            } else {
                boolean mkdirs = parentFile.mkdirs();
                if (mkdirs) {
                    return getFile(filePath);
                } else {
                    throw new OperationException("目录创建失败！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File getDiskFile(File file) {
        if (file.exists()) {
            return file;
        }
        return getFileWithDirs(file.getPath());
    }
}
