package com.liqun.power.annotation;

import java.lang.annotation.*;

/**
 *@Author: PowerQun
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotRepeatSubmit {

    /**
     * 过期时间，单位毫秒
     **/
    long value() default 5000;
}
