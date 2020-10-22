package com.liqun.power.aspect;

import com.alibaba.fastjson.JSON;
import com.liqun.power.entity.RequestLogErrorInfo;
import com.liqun.power.entity.RequestLogInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * PF-004
 *
 * @Author: PowerQun
 */
@Component
@Aspect
@Slf4j
public class RequestServerAspect {


    @Pointcut("execution(* com.liqun.power.controller..*(..))")
    public void requestServer() {
    }


    @Around("requestServer()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        RequestLogInfo logInfo = new RequestLogInfo();
        logInfo.setIP(request.getRemoteAddr());
        logInfo.setURL(request.getRequestURL().toString());
        logInfo.setHttpMethod(request.getMethod());
        logInfo.setClassMethod(proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName());
        logInfo.setRequestParams(getRequestParamsByProceedingJoinPoint(proceedingJoinPoint));

        Object result = proceedingJoinPoint.proceed();

        logInfo.setResult(result);
        logInfo.setTimeCost(System.currentTimeMillis() - start + " ms");
        log.info("Request Info      : {}", JSON.toJSONString(logInfo));
        return result;
    }


    @AfterThrowing(pointcut = "requestServer()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, RuntimeException e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        RequestLogErrorInfo requestErrorInfo = new RequestLogErrorInfo();
        requestErrorInfo.setIp(request.getRemoteAddr());
        requestErrorInfo.setUrl(request.getRequestURL().toString());
        requestErrorInfo.setHttpMethod(request.getMethod());
        requestErrorInfo.setClassMethod(String.format("%s.%s", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()));
        requestErrorInfo.setRequestParams(getRequestParamsByJoinPoint(joinPoint));
        requestErrorInfo.setException(e);
        log.info("Error Request Info      : {}", JSON.toJSONString(requestErrorInfo));
    }


    private Map<String, Object> getRequestParamsByProceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) {
        String[] parameterNames = ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        Object[] values = proceedingJoinPoint.getArgs();
        Map<String, Object> result = buildRequestParam(parameterNames, values);
        return result;
    }


    private Map<String, Object> getRequestParamsByJoinPoint(JoinPoint joinPoint) {
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] values = joinPoint.getArgs();
        Map<String, Object> result = buildRequestParam(parameterNames, values);
        return result;
    }


    private Map<String, Object> buildRequestParam(String[] paramNames, Object[] paramValues) {
        Map<String, Object> requestParams = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];

            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                value = file.getOriginalFilename();
            }

            requestParams.put(paramNames[i], value);
        }
        return requestParams;
    }
}
