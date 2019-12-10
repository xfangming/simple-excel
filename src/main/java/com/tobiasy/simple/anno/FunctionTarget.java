package com.tobiasy.simple.anno;

import com.tobiasy.simple.function.ExcelFunction;

import java.lang.annotation.*;

/**
 * @author tobiasy
 * @date 2019/7/18
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FunctionTarget {

    Class<? extends ExcelFunction> value() default ExcelFunction.class;

}
