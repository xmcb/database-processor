package com.juanpi.dbprocessor.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.juanpi.dbprocessor.core.EventHandler;
import com.juanpi.dbprocessor.key.RecoursKey;
import com.juanpi.dbprocessor.util.AddressUtil;
import com.juanpi.dbprocessor.util.RedisAPI;
import com.juanpi.sdk.api.ec.IEventListener;
import com.juanpi.sdk.ec.DefaultEventListenerFactory;

@Component("timeTask")
public class TimeTask {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private RedisAPI redisApi;
    
    @Resource
    private EventHandler eventHandler;
    
    @Resource
    private  AddressUtil addressUtil;
    
    
    private IEventListener eventListener = DefaultEventListenerFactory.getEventListener();
    
    //已监听的topic
    private Map<String,List<String>> localEventMap = new HashMap();
    
    
    /**
     * 获取topic
     * @return
     */
    private Map<String,List<String>> getTopicMap(){
        try {
            Map<String,List<String>> eventMap = redisApi.getObj(addressUtil.getLocalAppId(), Map.class);
            return eventMap;
        } catch (Exception e) {
            log.error("query topicMap exception", e);
        }
        return null;
    }
    
    /**
     * 是否已经订阅topic
     * @param eventMap
     * @return
     */
    private boolean getSubscribeState(Map<String,List<String>> eventMap){
        if(null != eventMap && eventMap.size() > 0){
            List<String> flagList = eventMap.get(RecoursKey.LIST_STATE_KEY);
            String flag = flagList.get(0);
            if(null != flag && flag.equals("1") && localEventMap.size() > 0){
                return true;
            }
            return false;
        }
        return true;
    }
    
    /**
     * 取消订阅topic
     * @param eventMap
     */
    private void cancelSubscribe(Map<String,List<String>> eventMap){
        //移除flag 
        eventMap.remove(RecoursKey.LIST_STATE_KEY);
        //取消订阅所有topic
        if(eventMap.size() < 1){
            for(Iterator<String> typeIterator = localEventMap.keySet().iterator();typeIterator.hasNext();){ 
                String type = typeIterator.next();
                List<String> localEventList = localEventMap.get(type);
                for(String localEvent:localEventList){
                    eventListener.unsubscribe(localEvent);
                }
                //删除本地缓存的topic
                localEventMap.remove(type);
            }
        }
    }
    /**
     * 移除重复type类型的消息
     * @param eventMap
     */
    private void removeRepeatTopic(Map<String,List<String>> eventMap){
        if(localEventMap.size() > 0){
            for(Iterator<String> typeIterator = localEventMap.keySet().iterator();typeIterator.hasNext();){
                String type = typeIterator.next();
                List<String> eventList = eventMap.get(type);
                if(null != eventList){
                    eventMap.remove(type);
                }
            }
        }
    }
    
    /**
     * 订阅新的topic
     * @param eventMap
     */
    private boolean subscribeNewTopic(Map<String,List<String>> eventMap){
        //订阅新的消息
        if(eventMap.size() > 0){
            for(Iterator<String> typeIterator = eventMap.keySet().iterator();typeIterator.hasNext();){
                String type = typeIterator.next();
                List<String> eventList = eventMap.get(type);
                for(String event:eventList){
                       subscribeNoOffsetTopic(event);
                }
                localEventMap.put(type, eventList);
            }
            
        }
        return true;
    }
    /**
     * 保存配置信息
     * @param eventMap
     */
    private void saveConfig(Map<String,List<String>> eventMap){
        //标识已订阅topic
        List<String> subscribeList = new ArrayList();
        subscribeList.add("1");
        eventMap.put(RecoursKey.LIST_STATE_KEY, subscribeList);
        //获取topic
        Map<String,List<String>> listMap = getTopicMap();
        listMap.remove(RecoursKey.LIST_STATE_KEY);
        eventMap.putAll(listMap);
        //存入redis
        redisApi.setObj(addressUtil.getLocalAppId(), eventMap);
    }
    
    
    /**
     * 订阅没有偏移量的topic
     * @param noOffsetTopic
     */
    private void subscribeNoOffsetTopic(String noOffsetTopic){
        try{
            eventListener.subscribe(noOffsetTopic, eventHandler);
            log.info("subscribe topic:" + noOffsetTopic);
        }catch(Exception e){
            log.error("subscribe noOffsetTopic exception:" + noOffsetTopic, e);
        }
    }
    
    
    /**
     * 订阅
     */
    public void subscribe(){
       try{
            //获取topic
            Map<String,List<String>> eventMap = getTopicMap();
            if(!getSubscribeState(eventMap)){
                //取消订阅
                cancelSubscribe(eventMap);
                //删除重复
                removeRepeatTopic(eventMap);
                //订阅新的消息
                if(subscribeNewTopic(eventMap)){
                   //保存配置信息
                   saveConfig(eventMap);
                }
            }
        }catch(Exception e){
            log.error("dbprocessor subscribe exception", e);
        }
    }
    
    /**
     * 心跳
     */
    public void heartbeat(){
        try {
            Map<String,Long> map = redisApi.getObj(RecoursKey.DBPROCESSOR_HEARTBEAT_KEY, Map.class);
            if(null == map){
                map = new HashMap();
                map.put(addressUtil.getLocalAppId(), System.currentTimeMillis());
            }else{
                map.put(addressUtil.getLocalAppId(), System.currentTimeMillis()); 
            }
            redisApi.setObj(RecoursKey.DBPROCESSOR_HEARTBEAT_KEY, map);
        } catch (Exception e) {
            log.error("dbprocessor heartbeat exception", e);
        }
    }
    
}
