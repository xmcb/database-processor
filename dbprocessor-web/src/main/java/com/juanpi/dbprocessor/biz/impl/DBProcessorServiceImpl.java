package com.juanpi.dbprocessor.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.juanpi.dbprocessor.biz.DBProcessorService;
import com.juanpi.dbprocessor.entity.DBProcessorEntity;
import com.juanpi.dbprocessor.key.RedisKey;
import com.juanpi.dbprocessor.util.RedisAPI;

@Service("dbProcessorService")
public class DBProcessorServiceImpl implements DBProcessorService {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private RedisAPI redisApi;
    
    @Override
    public List<DBProcessorEntity> findDBProcessorList() {
        Map<String,Long> dbprocessorHearbeatMap = redisApi.getObj(RedisKey.DBPROCESSOR_HEARTBEAT_KEY, Map.class);
        List<DBProcessorEntity> dbProcessorList = new ArrayList();
        if(null != dbprocessorHearbeatMap){
            Long localTime = System.currentTimeMillis();
            for (Iterator<String> iterator = dbprocessorHearbeatMap.keySet().iterator(); iterator.hasNext();) {
                String ip = iterator.next();
                Long heatbeatTime = dbprocessorHearbeatMap.get(ip);
                Long heatbeatInterval = localTime - heatbeatTime;
                Long count = heatbeatInterval/(1000*60);
                boolean alive = true;
                if(count > 1){
                    alive = false;
                }
                List<String> topicTypeList = new ArrayList();
                Map<String,List<String>> topicMap = redisApi.getObj(ip, Map.class);
                if(null != topicMap){
                    topicMap.remove(RedisKey.LIST_STATE_KEY);
                    topicTypeList.addAll(topicMap.keySet());
                }
                DBProcessorEntity dbprocessor = new DBProcessorEntity(ip, new Date(heatbeatTime), alive, topicTypeList);
                dbProcessorList.add(dbprocessor);
            }
            
        }
        return dbProcessorList;
    }

}
