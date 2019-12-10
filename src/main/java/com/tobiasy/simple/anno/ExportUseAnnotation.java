package com.tobiasy.simple.anno;

import java.lang.annotation.*;

/**
 * @author tobiasy
 * @date 2019/7/21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportUseAnnotation {

    String name() default "";

    int sheetAt() default 0;

    int firstRow() default 0;
}
