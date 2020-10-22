package com.liqun.power.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author: PowerQun
 */
@Service
public class HikariCPMonitorService {

    @Autowired
    @Qualifier("hikariCPDataSourceA")
    DataSource hikariCPDataSourceA;

    @Autowired
    @Qualifier("hikariCPDataSourceB")
    DataSource hikariCPDataSourceB;

    public void connector(){
        try {
            Connection connection = hikariCPDataSourceA.getConnection();
            Connection connection1 = hikariCPDataSourceB.getConnection();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
