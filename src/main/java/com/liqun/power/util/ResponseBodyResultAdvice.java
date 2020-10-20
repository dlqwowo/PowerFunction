package com.liqun.power.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;


/**
 * spring will add class which has @RestControllerAdvice to RequestResponseBodyAdviceChain
 * all requst will reach here before JsonViewResponseBodyAdvice
 * @Author: PowerQun
 */
@RestControllerAdvice
public class ResponseBodyResultAdvice implements ResponseBodyAdvice {

    /**
     * if return true ,beforeBodyWrite will be used
     *
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        Method method = methodParameter.getMethod();
        com.liqun.power.annotation.ResponseBodyResultAdvice annotation = method.getAnnotation(com.liqun.power.annotation.ResponseBodyResultAdvice.class);
        if (annotation != null) {
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }

}
