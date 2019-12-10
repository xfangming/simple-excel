package com.tobiasy.simple.anno;

import com.tobiasy.simple.function.ExcelFunction;

import java.lang.annotation.*;

/**
 * @author tobiasy
 * @date 2019/7/21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportUseAnnotation {

    String value() default "";

    int firstRow() default 0;

    int sheetAt() default 0;
}
