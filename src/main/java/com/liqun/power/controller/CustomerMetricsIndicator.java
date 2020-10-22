package com.liqun.power.controller;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * PF-003
 * Endpoint即支持JMX也支持http。
 * WebEndpoint只支持http。
 *
 * @ReadOperation：GET请求
 * @WriteOperation：POST请求
 * @DeleteOperation：DELETE请求
 * @Author: PowerQun
 */

//http://localhost:8080/actuator/customer
@WebEndpoint(id = "customer")
@Component
public class CustomerMetricsIndicator {

    @ReadOperation
    public Map<String, Object> time() {
        Map<String, Object> result = new HashMap<>();
        Date time = new Date();
        result.put("当前时间:", time.toString());
        return result;
    }
}
