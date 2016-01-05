package com.juanpi.dbprocessor.database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

@Repository("conectionPool")
public class ConnectionPoolFactory {
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private DruidDataSource dataSource;
    
    @Resource
    private DruidDataSource dbprocessorDataSource;

    public Connection getConnection(){
        DruidPooledConnection connetion = null;
        try {
            connetion = dataSource.getConnection();
        } catch (SQLException e) {
            log.error("dataSource.getConnection exception", e);
        }
        return connetion;
    }
    
    public Connection getErrorConnection(){
        DruidPooledConnection connetion = null;
        try {
            connetion = dbprocessorDataSource.getConnection();
        } catch (SQLException e) {
            log.error("dataSource.getConnection exception dbprocessor", e);
        }
        return connetion;
    }
    
}
