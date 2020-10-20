package com.liqun.power.annotation;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;
/**
 *@Author: PowerQun
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseBody
public @interface ResponseBodyResultAdvice {
}
