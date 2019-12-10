package com.tobiasy.simple.bean;

import com.tobiasy.simple.constants.DateConst;
import com.tobiasy.simple.function.ExcelFunction;

import java.util.function.Function;

/**
 * @author tobiasy
 * @date 2019/7/19
 */
public class SubExcelFunction extends ExcelFunction {
    public Function<String, Enum> TO_SEX = getEnumName(SexEnum.class);
    public Function<String, Enum> TO_FAVOURITE = getEnumValue(Favourite.class);
    @Override
    public String getDefaultPattern() {
        //return DateConst.SIMPLE_DATE_PATTERN;
        return DateConst.DATE_PATTERN;
    }
}