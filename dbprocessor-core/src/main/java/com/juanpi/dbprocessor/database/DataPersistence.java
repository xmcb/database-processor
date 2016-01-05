package com.juanpi.dbprocessor.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.juanpi.dbprocessor.core.ReflexEvent;
import com.juanpi.dbprocessor.error.ErrorCode;
import com.juanpi.dbprocessor.util.JsonMapper;
import com.juanpi.sdk.ec.msg.ECConstants;
import com.juanpi.sdk.ec.msg.Event;


@Repository("dataPersistence")
public class DataPersistence {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private ReflexEvent reflexEvent;
    
    @Resource
    private ConnectionPoolFactory connetionPool;
    
    
    public void execute(Event event, Object obj){
        //执行sql
        executeSql(event, obj);
    }
    
    private void executeSql(Event event, Object obj){
        Connection connection = null;
        PreparedStatement statement = null;
        String sql = null;
        List<Object> parameterList = null;
        try {
            connection = connetionPool.getConnection();
            Map<String, List<Object>> tableMap = reflexEvent.reflex(obj);
            if(null != tableMap && tableMap.size() > 0){
                sql = tableMap.keySet().iterator().next();
                parameterList = tableMap.get(sql);
            }else{
                //记录错误数据AtomicInteger
                error(event, obj, ErrorCode.REFLEX_ORM_ERROR);
                return;
            }
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < parameterList.size(); i++) {
                statement.setObject(i + 1, parameterList.get(i));
            }
            statement.execute();
            log.info("execute sql:" + sql + ";parameterList:" + JsonMapper.toJson(parameterList) + ";topic:"+event.getTopic()+";queueId:" + event.getQueueId()+";offset:" + event.getOffset());
        } catch (Exception e) {
            log.error("execute sql exception:'" + sql + "';parameterList:'" + JsonMapper.toJson(parameterList) + "';topic:"+event.getTopic()+";queueId:" + event.getQueueId() + ";offset:" + event.getOffset(), e);
            //记录错误数据
            error(event, obj, ErrorCode.EXECUTE_SQL_ERROR);
        } finally{
            try {
                if(null != statement)
                    statement.close();
                if(null != connection)
                    connection.close();
            } catch (SQLException e) {
                log.error("close connetion and statement exception", e);
            }
            event = null;
            obj = null;
        }
    }
    
    public void error(Event event, Object obj, int state){
        Connection connection = null;
        //AtomicInteger
        PreparedStatement statement = null;
        String errorSql = "insert into jp_dbprocessor_error(class_name,send_nanos,send_date,eventid,transactionid,error_code,data_json,sql_hendle,sql_parameter,event_json) values(?,?,?,?,?,?,?,?,?,?)";
        try {
            String className = null!=obj?obj.getClass().getName():"obj";
            String send_milis = event.getProperty(ECConstants.EVENT_SEND_MILIS);
            Date sendDate = ((null==send_milis)?new Date():new Date(Long.parseLong(send_milis)));
            String sendNanos = event.getProperty(ECConstants.EVENT_SEND_NANOS);
            sendNanos = ((null==sendNanos)?System.nanoTime()+"":sendNanos);
            String eventId = event.getEventId();
            String transactionId = event.getTransactionId();
            String eventDataJson = "";
            String sqle = "";
            String sqlParameter = "";
            String mqEventJson = JsonMapper.toJson(event);
            if(null != obj){
                eventDataJson = JsonMapper.toJson(obj); 
                Map<String, List<Object>> tableMap = reflexEvent.reflex(obj);
                if(null != tableMap && tableMap.size() > 0){
                    sqle = tableMap.keySet().iterator().next();
                    sqlParameter = JsonMapper.toJson(tableMap.get(sqle));
                }
            }else{
                Object objList = event.getEvent();
                if(null != objList && objList instanceof List){
                    List<Object> eventList = (List<Object>)objList;
                    for (Object object : eventList) {
                        eventDataJson += JsonMapper.toJson(object) + ">"; 
                        Map<String, List<Object>> tableMap = reflexEvent.reflex(object);
                        if(null != tableMap && tableMap.size() > 0){
                            String sqlTemp = tableMap.keySet().iterator().next();
                            sqle +=  (sqlTemp + " >");
                            sqlParameter += JsonMapper.toJson(tableMap.get(sqlTemp)) + ">";
                        }
                    }
                }
            }
            connection = connetionPool.getErrorConnection();
            statement = connection.prepareStatement(errorSql);
            statement.setObject(1, className);
            statement.setObject(2, sendNanos);
            statement.setObject(3, sendDate);
            statement.setObject(4, eventId);
            statement.setObject(5, transactionId);
            statement.setObject(6, state);
            statement.setObject(7, eventDataJson);
            statement.setObject(8, sqle);
            statement.setObject(9, sqlParameter);
            statement.setObject(10, mqEventJson);
            statement.execute();
            log.info("execute errorSql:" + errorSql);
        } catch (Exception e) {
            log.error("execute errorSql excepiton:'" + errorSql + "';state:" + state, e);
        } finally{
            try {
                if(null != statement)
                    statement.close();
                if(null != connection)
                    connection.close();
            } catch (SQLException e) {
                log.error("DataPersistence.error close connetion and statement exception", e);
            }
        }
    }
    
    
    
}
