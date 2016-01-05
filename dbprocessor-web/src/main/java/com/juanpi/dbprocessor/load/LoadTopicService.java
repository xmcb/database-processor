package com.juanpi.dbprocessor.load;

import java.io.InputStream;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.juanpi.dbprocessor.biz.TopicService;


@Component("loadTopicService")
public class LoadTopicService implements ApplicationListener<ContextRefreshedEvent>{

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private TopicService topicService;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadTopic();
    }
    
    public void loadTopic(){
        InputStream in = LoadTopicService.class.getClassLoader().getResourceAsStream("topic.properties");
        if(null != in){
            topicService.saveTopic(in);
        }else{
            log.error("not found topic.properties！");
        }
    }
    
}
