package com.tobiasy.simple.utils;

import com.tobiasy.simple.enums.ClassEnum;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tobiasy
 * @date 2019/3/4
 */
public class ClassParser {

    public static <T> T[] parseObject(Object[] objects, Class<T> clazz, T[] result) {
        for (int i = 0; i < objects.length; i++) {
            result[i] = parse(objects[i], clazz);
        }
        return result;
    }

    public static <T> List<T> parseObject(Object[] objects, Class<T> tClass) {
        List<T> list = new ArrayList<>();
        for (Object object : objects) {
            list.add(parse(object, tClass));
        }
        return list;
    }

    public static <T> T parseByType(Object value, Class<T> clazz) {
        return parse(value, clazz);
    }

    public static <T> T parse(Object value, Class<T> clazz) {
        Object type = getType(value, clazz);
        T t = (T) type;
        return t;
    }


    /**
     * 将Object对象转化为Class指定类型
     *
     * @param value Object原始对象
     * @param clazz 转化类型
     * @return 转化后的Object对象
     */
    public static Object getType(Object value, Class clazz) {
        ClassEnum classEnum = ClassEnum.getInstance(clazz);
        switch (classEnum) {
            case STRING:
                return String.valueOf(value);
            case BYTE:
                BigDecimal decimal = new BigDecimal(value.toString());
                return decimal.byteValue();
            case SHORT:
                return new BigDecimal(value.toString()).shortValue();
            case INT:
            case INTEGER:
                return parseInt(value);
            case LONG:
            case LONG_MAX:
                return parseLong(value.toString());
            case BOOLEAN:
            case BOOLEAN_MAX:
                return parseBoolean(value.toString());
            case DOUBLE:
            case DOUBLE_MAX:
                return new BigDecimal(value.toString()).doubleValue();
            case FLOAT:
            case FLOAT_MAX:
                return new BigDecimal(value.toString()).floatValue();
            case DATE:
                return parseDate(value);
            case CHAR:
            case CHARACTER:
                return parseChar(value);
            default:
                return value;
        }
    }

    /**
     * 字符串转化为Integer，如果带有小数点自动忽略
     *
     * @param s
     * @return
     */
    public static Integer parseInt(String s) {
        Number parse = NumericUtils.parse(s);
        if (parse != null) {
            return parse.intValue();
        }
        return null;
    }

    public static Integer parseInt(Long s) {
        return new BigDecimal(s).intValue();
//        return parseInt(String.valueOf(s));
    }

    public static Integer parseInt(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof Long) {
            return parseInt((Long) o);
        } else if (o instanceof Character) {
            return (int) (char) o;
        }
        return parseInt(String.valueOf(o));
    }

    public static char parseChar(Object value) {
        if (value instanceof Integer) {
            int i = (int) value;
            return (char) i;
        } else {
            return (Character) value;
        }
    }

    public static Byte parseByte(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return Byte.parseByte((String) object);
        } else if (object instanceof Integer) {
            return Byte.parseByte(String.valueOf(object));
        }
        return (Byte) object;
    }

    public static Short parseShort(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return Short.parseShort((String) object);
        } else if (object instanceof Integer) {
            return Short.parseShort(String.valueOf(object));
        }
        return (Short) object;
    }

    public static Long parseLong(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return Long.parseLong((String) object);
        } else if (object instanceof Long) {
            return (Long) object;
        } else {
            return Long.parseLong(String.valueOf(object));
        }
    }

    public static Double parseDouble(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof BigDecimal) {
            BigDecimal decimal = (BigDecimal) object;
            return decimal.doubleValue();
        } else if (object instanceof Double) {
            return (double) object;
        } else if (object instanceof String) {
            return Double.parseDouble((String) object);
        } else {
            return Double.parseDouble(String.valueOf(object));
        }
    }

    public static Float parseFloat(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof BigDecimal) {
            BigDecimal decimal = (BigDecimal) object;
            return decimal.floatValue();
        } else if (object instanceof Float) {
            return (Float) object;
        } else if (object instanceof String) {
            return Float.parseFloat((String) object);
        } else {
            return Float.parseFloat(String.valueOf(object));
        }
    }

    public static Boolean parseBoolean(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return Boolean.parseBoolean((String) o);
        } else if (o instanceof Integer) {
            Integer i = (Integer) o;
            return i == 1;
        } else {
            return (Boolean) o;
        }
    }

    public static Date parseDate(Object value) {
        if (value instanceof Long) {
            return DateUtils.stampToDate((Long) value);
        } else if (value instanceof String) {
            Date parse = null;
            try {
                parse = DateFormat.getDateInstance().parse(value.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return parse;
        } else {
            return (Date) value;
        }
    }
} 