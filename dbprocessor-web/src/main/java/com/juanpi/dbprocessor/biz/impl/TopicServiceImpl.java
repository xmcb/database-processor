package com.juanpi.dbprocessor.biz.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.juanpi.dbprocessor.biz.DBProcessorService;
import com.juanpi.dbprocessor.biz.TopicService;
import com.juanpi.dbprocessor.entity.DBProcessorEntity;
import com.juanpi.dbprocessor.entity.TopicEntity;
import com.juanpi.dbprocessor.entity.TypeEntity;
import com.juanpi.dbprocessor.key.RedisKey;
import com.juanpi.dbprocessor.util.RedisAPI;

@Service("topicService")
public class TopicServiceImpl implements TopicService {

    @Resource
    private RedisAPI redisApi;
    
    @Resource
    private DBProcessorService dbProcessorService;
    
    @Override
    public List<TopicEntity> findTopicByType(String topicType) {
        Map<String,List<String>> topicListMap = redisApi.getObj(RedisKey.TOPIC_DATA_KEY, Map.class);
        List<TopicEntity> topicEntityList = new ArrayList();
        if(null != topicListMap){
            List<String> topicList = topicListMap.get(topicType);               
            for (String topic : topicList) {
                TopicEntity topicEntity = new TopicEntity(topicType, topic);
                topicEntityList.add(topicEntity);
            }
        }
        return topicEntityList;
    }

    @Override
    public List<TopicEntity> findAllTopic() {
        Map<String,List<String>> topicListMap = redisApi.getObj(RedisKey.TOPIC_DATA_KEY, Map.class);
        List<TopicEntity> topicEntityList = new ArrayList();
        if(null != topicListMap){
            for(Iterator<String> types = topicListMap.keySet().iterator();types.hasNext();){
                String type = types.next();
                List<String> topicList = topicListMap.get(type);
                for (String topic : topicList) {
                    TopicEntity topicEntity = new TopicEntity(type, topic);
                    topicEntityList.add(topicEntity);
                }
            }
        }
        return topicEntityList;
    }

    @Override
    public List<TypeEntity> getFreeTopicType(String host){
        List<TypeEntity> typeEntityList = new ArrayList();
        List<String> topicTypeList = getAllTopicType();
        for (String type : topicTypeList) {
            //只加载当前host对应的topic类型(一个dbprocessor只监听一个topic类型)
            if(host.indexOf(type) >= 0){
                TypeEntity typeEntity = new TypeEntity(type, 0);
                typeEntityList.add(typeEntity);
            }
        }
        List<DBProcessorEntity> dbProcessorList = dbProcessorService.findDBProcessorList();
        for (DBProcessorEntity dbProcessorEntity : dbProcessorList) {
            List<String> typeList = dbProcessorEntity.getTopicTypeList();
            if(null != typeList){
                for (String type : typeList) {
                    for(TypeEntity typeEntity : typeEntityList){
                        if(typeEntity.getName().equals(type)){
                            if(dbProcessorEntity.getHost().equals(host)){
                                typeEntity.setState(1);
                            }else{
                                typeEntity.setState(2); 
                            }
                            break;
                        }
                    }
                }
            }
        }
        return typeEntityList;
    }
    
    @Override
    public List<String> getAllTopicType() {
        Map<String,List<String>> topicListMap = redisApi.getObj(RedisKey.TOPIC_DATA_KEY, Map.class);
        List<String> typeList = new ArrayList();
        if(null != topicListMap){
            typeList.addAll(topicListMap.keySet());
        }
        return typeList;
    }

    @Override
    public void saveTopic(InputStream in) {
        Map<String,List<String>> topicListMap = new HashMap();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String topic = null;
        try {
            while(null != (topic = reader.readLine())){
                String[] topics = topic.split("\\.");
                if(null == topics || topics.length <= 1 || topic.length() <= 0 || topic.indexOf(".") <= 0 || topic.indexOf("#") >= 0){
                    continue;
                }
                String type = "";
                /*String types="cart,goods,mkt,order,user";
                for(int i=0;i<topics.length;i++){
                    if(types.indexOf(topics[i]) >= 0){
                        type = topics[i];
                        break;
                    }
                }*/
                type = topics[0];
               
                List<String> topicList = topicListMap.get(type);
                if(null == topicList || topicList.size() <= 0){
                    topicList = new ArrayList();
                }
                topicList.add(topic);
                topicListMap.put(type, topicList);
            }
            in.close();
            reader.close();
            redisApi.setObj(RedisKey.TOPIC_DATA_KEY, topicListMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pushTopicConfig(String host, String... types) {
        Map<String,List<String>> topicListMap = redisApi.getObj(RedisKey.TOPIC_DATA_KEY, Map.class);
        Map<String,List<String>> pushTopicMap = new HashMap();
        List<String> flagList = new ArrayList();
        flagList.add("0");
        pushTopicMap.put(RedisKey.LIST_STATE_KEY, flagList);
        if(null != types){
            for(String type : types){
                pushTopicMap.put(type,topicListMap.get(type));
            }
        }
        redisApi.setObj(host, pushTopicMap);
    }

}
