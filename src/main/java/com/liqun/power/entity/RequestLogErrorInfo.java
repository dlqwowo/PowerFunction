package com.liqun.power.entity;

import lombok.Data;

/**
 * @Author: PowerQun
 */
@Data
public class RequestLogErrorInfo {
    private String ip;
    private String url;
    private String httpMethod;
    private String classMethod;
    private Object requestParams;
    private RuntimeException exception;
}
