package com.liqun.power.controller;

import com.liqun.power.service.HikariCPMonitorService;
import com.liqun.power.util.Result;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: PowerQun
 * PF-003
 */
@RestController
@RequestMapping("/hikari")
public class HikariCPMonitorController {

    @Autowired
    public HikariCPMonitorService hikariCPMonitorService;

    @GetMapping("/connection")
    public Result connection() {
        hikariCPMonitorService.connector();
        return Result.success("ok");
    }

    @GetMapping("/getMonitorParameter")
    public Result<Map<String, Object>> getMonitorParameter() throws MalformedObjectNameException {
        Map<String, Object> map = new HashMap<>();
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName poolName = new ObjectName("com.zaxxer.hikari:type=Pool (pool-1)");
        HikariPoolMXBean poolProxy = JMX.newMXBeanProxy(mBeanServer, poolName, HikariPoolMXBean.class);
        HikariPoolMXBean poolProxy2 = JMX.newMXBeanProxy(mBeanServer, poolName, HikariPoolMXBean.class);
        int idleConnections = poolProxy.getIdleConnections();
        int activeConnections = poolProxy.getActiveConnections();
        int threadsAwaitingConnection = poolProxy.getThreadsAwaitingConnection();
        int totalConnections = poolProxy.getTotalConnections();

        map.put("idleConnections", idleConnections);
        map.put("activeConnections", activeConnections);
        map.put("threadsAwaitingConnection", threadsAwaitingConnection);
        map.put("totalConnections", totalConnections);
        return Result.success(map);
    }


}
