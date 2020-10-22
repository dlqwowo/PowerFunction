package com.liqun.power.entity;

import lombok.Data;

import java.util.Map;

/**
 * @Author: PowerQun
 */
@Data
public class RequestLogInfo {

    private String IP;
    private String URL;
    private String HttpMethod;
    private String ClassMethod;
    private Map<String, Object> RequestParams;
    private Object result;
    private String TimeCost;

}
