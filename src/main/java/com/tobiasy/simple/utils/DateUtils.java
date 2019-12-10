package com.tobiasy.simple.utils;

import com.tobiasy.simple.constants.DateConst;
import com.tobiasy.simple.exception.OperationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author tobiasy
 */
public class DateUtils {
    private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();

    private static String defaultPattern = DateConst.YYYY_MM_DD_HH_MM_SS;

    public static String format(Date date, String pattern) {
        return getSimpleDateFormat(pattern).format(date);
    }

    public static String format(Date date) {
        return format(date, defaultPattern);
    }

    public static String format(Object date) {
        return format(date, defaultPattern);
    }

    public static String format(Object date, String defaultFormat) {
        return getSimpleDateFormat(defaultFormat).format(date);
    }

    public static Date parse(String str, String pattern) {
        Date parse;
        try {
            parse = getSimpleDateFormat(pattern).parse(str);
        } catch (ParseException e) {
            throw new OperationException("%s不能按照格式%s转化！", str, pattern);
        }
        return parse;
    }

    public static Date parse(String text) {
        return parse(text, defaultPattern);
    }

    public static Date parse(Object value, String pattern) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return stampToDate((Long) value);
        } else if (value instanceof String) {
            try {
                long l = Long.parseLong(value.toString());
                return stampToDate(l);
            } catch (Exception e) {
                return parse(value.toString(), pattern);
            }
        }
        return (Date) value;
    }

    public static Date parse(Object value) {
        return parse(value, defaultPattern);
    }

    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        SimpleDateFormat sdf = threadLocal.get();
        if (sdf == null) {
            threadLocal.remove();
            sdf = new SimpleDateFormat(pattern, Locale.US);
            threadLocal.set(sdf);
        }
        return sdf;
    }

    public static SimpleDateFormat getSimpleDateFormat() {
        return getSimpleDateFormat(defaultPattern);
    }

    public static String getCurrentDateString() {
        return format(new Date());
    }

    /**
     * 将时间戳转化为时间字符串
     *
     * @param lt
     * @param pattern
     * @return
     */
    public static String stampToDateString(Long lt, String pattern) {
        Date date = new Date(lt);
        return format(date);
    }

    public static LocalDate parseToLocalDate(String text, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(text, formatter);
    }

    public static String formatLocalDate(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(date);
    }

    public static Date stampToDate(String s) {
        Long lt = new Long(s);
        return new Date(lt);
    }

    public static Date stampToDate(Long lt) {
        return new Date(lt);
    }

    /**
     * 将时间转换为时间戳
     *
     * @param s
     * @return
     */
    public static String dateToStamp(String s, String pattern) {
        Date date = parse(s);
        return dateToStamp(date);
    }

    public static String dateToStamp(Date date) {
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    /**
     * 根据时分秒创建时间对象
     *
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return
     */
    public static Date getDate(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

}
