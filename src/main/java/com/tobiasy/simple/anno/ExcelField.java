package com.tobiasy.simple.anno;

import java.lang.annotation.*;

/**
 * @author tobiasy
 * @date 2019/7/18
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelField {
    String name() default "";

    int index() default 0;

    String function() default "TO_STRING";

    int columnSize() default 1;

    String dateFormat() default "";
}
