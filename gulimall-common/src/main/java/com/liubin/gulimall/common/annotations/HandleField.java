package com.liubin.gulimall.common.annotations;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HandleField {

    String value() default "";
}
