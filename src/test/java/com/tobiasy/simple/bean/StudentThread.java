package com.tobiasy.simple.bean;

import com.tobiasy.simple.excel.ExportBuilder;
import com.tobiasy.simple.utils.ClassLoaderUtils;
import com.tobiasy.simple.utils.Out;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tobiasy
 * @date 2019/7/20
 */
public class StudentThread implements Runnable {
    @Override
    public void run() {
        List<Long> integers = new ArrayList<>();
        for (Integer i = 0; i < 100; i++) {
            synchronized (i) {
                long start = System.nanoTime();
                boolean f = ExportBuilder.createExcel(
                        ClassLoaderUtils.getLoaderFile("simple-user.xml"),
                        new File("F:/test/thread/simple-user" + i + ".xlsx"),
                        ObjectData.getUserList(6552));
                long end = System.nanoTime();
                long l = TimeUnit.NANOSECONDS.toMillis(end - start);
                Out.println("%s : %s -> %s 耗时%s毫秒",
                        Thread.currentThread().getName(), i, f, l);
                integers.add(l);
            }
        }
        integers.stream().map(Long::intValue);
    }

    public static void main(String[] args) {
        StudentThread thread = new StudentThread();
        new Thread(thread).start();
        new Thread(thread).start();
        new Thread(thread).start();
        new Thread(thread).start();

    }
}
