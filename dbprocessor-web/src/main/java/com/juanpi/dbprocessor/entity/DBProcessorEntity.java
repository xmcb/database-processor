package com.juanpi.dbprocessor.entity;

import java.util.Date;
import java.util.List;


public class DBProcessorEntity {

    private String host;
    
    private Date hearBeatTime;
    
    private boolean alive;
    
    private List<String> topicTypeList;
    
    
    public DBProcessorEntity(){
        
    }

    public DBProcessorEntity(String host, Date hearBeatTime, boolean alive, List<String> topicTypeList){
        this.host = host;
        this.hearBeatTime = hearBeatTime;
        this.alive = alive;
        this.topicTypeList = topicTypeList;
    }

    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public Date getHearBeatTime() {
        return hearBeatTime;
    }

    public void setHearBeatTime(Date hearBeatTime) {
        this.hearBeatTime = hearBeatTime;
    }
    
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    public List<String> getTopicTypeList() {
        return topicTypeList;
    }
    
    public void setTopicTypeList(List<String> topicTypeList) {
        this.topicTypeList = topicTypeList;
    }
    
}
