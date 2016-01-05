package com.juanpi.dbprocessor.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.juanpi.dbprocessor.database.DataPersistence;
import com.juanpi.dbprocessor.error.ErrorCode;
import com.juanpi.sdk.api.ec.IEventHandler;
import com.juanpi.sdk.ec.msg.Event;
import com.juanpi.sdk.ec.msg.EventQuery;

@Component("eventHandler")
public class EventHandler implements IEventHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private ConcurrentStorageHandler concurrentStorageHandler;
    
    @Resource
    private DataPersistence dataPersistence;
    
    private static ConcurrentMap<String, Map<Long,Map<Long, Long>>> eventMaps = new ConcurrentHashMap();

    public void handleEvent(final Event event) {
        try {
            /*log.info("traceID:" + event.getTraceId() + ";eventId:" + event.getEventId());
            if(event.getEvent() == null){
                log.error("eventId:" + event.getEventId() + " data is null");
                return;
            }
            //192.168.139.92-com.juanpi.dbprocessor.sequence.DBEventSequence>1439447718136>2
            String eventId = event.getEventId();
            String timeStamp = event.getProperty(ECConstants.EVENT_SEND_MILIS);
            //event发送时间
            Long eventTime = Long.parseLong(timeStamp);
            String[] eventIds = eventId.split(">");
            //当前event版本
            Long eventVersion = Long.parseLong(eventIds[2]);
            //已处理的event版本信息
            Map<Long,Map<Long, Long>> eventMap = eventMaps.get(eventIds[0]);
            if(null != eventMap){
                Map<Long,Long> versionMap = eventMap.get(Long.parseLong(eventIds[1]));
                if(null != versionMap){
                    //处理版本错误的event
                    Integer errorCode = versionErrorEventHandle(event, eventIds, versionMap, eventVersion, eventTime);
                    if(errorCode > 0){
                        return;
                    }
                }else{
                    //处理老版本的event
                    Integer errorCode = oldEventHandle(event, eventIds[0], eventTime, eventMap);
                    if(errorCode > 0){
                        return;
                    }
                }
            } 
            //缓存版本信息
            setCacheEventMaps(eventIds[0], Long.parseLong(eventIds[1]), eventVersion, eventTime);*/
            //log.info("begin processor event:" + event.getEventId());
            //插库
            concurrentStorageHandler.execute(event);
        } catch (Exception e) {
            log.error("handleEvent exception", e);
            concurrentStorageHandler.error(event,ErrorCode.CACHE_VERSION_ERROR);
        }
    }

    private int versionErrorEventHandle(final Event event, String[] eventIds, Map<Long,Long> versionMap, Long eventVersion,Long eventTime) {
        Long cacheVersion = versionMap.keySet().iterator().next();
        Long cacheEventTime = versionMap.get(cacheVersion);
        ++cacheVersion;
        //过期event
        if (eventTime < cacheEventTime) {
            concurrentStorageHandler.error(event, ErrorCode.RETURNSON_EVENT_ERROR);
            return ErrorCode.RETURNSON_EVENT_ERROR;
        }
        //非法的重复version
        if(eventTime >= cacheEventTime && eventVersion < cacheVersion){
            concurrentStorageHandler.error(event, ErrorCode.ILLEGAL_VERSION_ERROR);
            return ErrorCode.ILLEGAL_VERSION_ERROR;
        }
        //当前version大于缓存version
        if(eventTime >= cacheEventTime && eventVersion > cacheVersion){
            //循环拉取没有处理的event
            while(true){
                String onOneEventId = eventIds[0] + ">" + eventIds[1] + ">" + cacheVersion;
                ++cacheVersion;
                //上一个版本event
                Event onOneEvent = EventQuery.queryEvent(event.getTopic(), onOneEventId);
                if(null != onOneEvent){
                    handleEvent(onOneEvent);
                }else{
                    //缓存记录跳过的版本信息
                    setCacheEventMaps(eventIds[0], Long.parseLong(eventIds[1]), cacheVersion - 1, 1L);
                    byte[] data = null;
                    onOneEvent = new Event(onOneEventId, null, event.getTopic(), data);
                    concurrentStorageHandler.error(onOneEvent, ErrorCode.PULL_EVENT_ERROR);
                }
                if(cacheVersion.equals(eventVersion)){
                    return -1;
                }
            }
        }
        return -1;
    }

    private Integer oldEventHandle(final Event event, String eventKey, Long eventTime, Map<Long, Map<Long, Long>> eventMap) {
        Long oldVersionKey = eventMap.keySet().iterator().next();
        Map<Long, Long>  oldVersionMap = eventMap.get(oldVersionKey);
        Long oldVersion = oldVersionMap.keySet().iterator().next();
        Long oldTime = oldVersionMap.get(oldVersion);
        if(oldTime > eventTime){
            concurrentStorageHandler.error(event, ErrorCode.RETURNSON_EVENT_ERROR);
            return ErrorCode.RETURNSON_EVENT_ERROR;
        }
        int count = 0;
        while(true){
            String oldEventId = eventKey + ">" + oldVersionKey + ">" + oldVersion;
            ++oldVersion;
            //上一个版本event
            Event oldEvent = EventQuery.queryEvent(event.getTopic(), oldEventId);
            if(null != oldEvent){
                handleEvent(oldEvent);
            }else{
                //缓存记录跳过的版本信息
                setCacheEventMaps(eventKey, oldVersionKey, oldVersion - 1, 1L);
                //往后衍生3个版本
                ++count;
                byte[] data = null;
                oldEvent = new Event(oldEventId, null, event.getTopic(), data);
                concurrentStorageHandler.error(oldEvent, ErrorCode.EXPAND_EVENT_ERROR);
                if(count == 3){
                    eventMaps.remove(eventKey);
                    break;
                }
            }
        }
        return -1;
    }

    private static synchronized void setCacheEventMaps(String eventKey, Long versionKey, Long eventVersion, Long eventTime){
       //缓存记录跳过的版本信息
        Map<Long,Long> versionMap = new HashMap();
        versionMap.put(eventVersion, eventTime);
        Map<Long,Map<Long, Long>> eventMap = new HashMap();
        eventMap.put(versionKey, versionMap);
        eventMaps.put(eventKey, eventMap);
    }
    
    public ConcurrentStorageHandler getConcurrentStorageHandler() {
        return concurrentStorageHandler;
    }
    
    public void setConcurrentStorageHandler(ConcurrentStorageHandler concurrentStorageHandler) {
        this.concurrentStorageHandler = concurrentStorageHandler;
    }

}
