package com.juanpi.dbprocessor.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import com.juanpi.dbprocessor.database.DataPersistence;
import com.juanpi.dbprocessor.error.ErrorCode;
import com.juanpi.sdk.ec.msg.Event;

@Component("concurrentStorageHandler")
public class ConcurrentStorageHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private DataPersistence dataPersistence;
    
    @Resource
    private ReflexEvent reflexEvent;
    
    private static Map<Integer,ExecutorService> threadPoolMap;
    
    private ExecutorService executor = Executors.newFixedThreadPool(20);
    
    public ConcurrentStorageHandler(int threads){
        threadPoolMap = new HashMap();
        for (int i = 0; i <= threads; i++) {
            threadPoolMap.put(i, Executors.newFixedThreadPool(1));
        }
    }
    
    public void execute(final Event event){
        try {
            List<Object> objList = event.getEvent(List.class);
            log.info("eventId:"+event.getEventId()+ ";message size:" + objList.size());
            for (final Object object : objList) {
                //计算线程池ID
                Integer threadId = reflexEvent.getThreadId(object);
                threadPoolMap.get(threadId).execute(new Runnable() {
                    public void run() {
                        dataPersistence.execute(event,object);
                    }
                });
            }
        } catch (Exception e) {
            log.error("reflex event exception", e);
            error(event, ErrorCode.ROUTE_THREAD_ERROR);
        }
    }
    
    public void error(final Event event,final Integer state){
        //记录跳过的event到数据库
        executor.execute(new Runnable() {
          @Override
          public void run() {
              dataPersistence.error(event, null, state);                                
          }
        });
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
