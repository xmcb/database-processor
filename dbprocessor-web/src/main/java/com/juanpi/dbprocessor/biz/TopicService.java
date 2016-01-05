package com.juanpi.dbprocessor.biz;

import java.io.InputStream;
import java.util.List;

import com.juanpi.dbprocessor.entity.TopicEntity;
import com.juanpi.dbprocessor.entity.TypeEntity;


public interface TopicService {

    public List<TopicEntity> findTopicByType(String topicType);
    
    public List<TopicEntity> findAllTopic();
    
    public List<TypeEntity> getFreeTopicType(String host);
    
    public List<String> getAllTopicType();
    
    public void saveTopic(InputStream in);
    
    public void pushTopicConfig(String host, String... types);
    
    
}
