package com.liqun.power.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Author: PowerQun
 */
@Configuration
public class HikariCPConfig {

    @Bean(name = "hikariCPDataSourceA")
    public DataSource HikariCPDataSourceA() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:oracle:thin:@140.231.89.85:30087:xe");
        ds.setUsername("IEC4SNC");
        ds.setPassword("oracle");
        ds.setDriverClassName("oracle.jdbc.OracleDriver");
        ds.setRegisterMbeans(true);
        //ds.setSchema("IEC4SNC");
        ds.setMaximumPoolSize(20);
        ds.setIdleTimeout(60000);
        ds.setMinimumIdle(60000);
        ds.setPoolName("pool-1");
        return ds;
    }


    @Bean(name = "hikariCPDataSourceB")
    public DataSource HikariCPDataSourceB() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:oracle:thin:@140.231.89.85:30087:xe");
        ds.setUsername("IEC4SNC");
        ds.setPassword("oracle");
        ds.setDriverClassName("oracle.jdbc.OracleDriver");
        ds.setRegisterMbeans(true);
        //ds.setSchema("IEC4SNC");
        ds.setMaximumPoolSize(20);
        ds.setIdleTimeout(60000);
        ds.setMinimumIdle(60000);
        ds.setPoolName("pool-2");
        return ds;
    }
}
