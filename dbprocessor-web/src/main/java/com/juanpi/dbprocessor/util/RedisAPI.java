package com.juanpi.dbprocessor.util;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component("redisApi")
public class RedisAPI {
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private SerializerUtil serializer;
    
    private JedisPool pool;
    
    @Value("${jedis.host}")
    private String host;
    
    @Value("${jedis.port}")
    private int port;
    
    public Jedis getJedis() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(100);
            config.setMaxTotal(500);
            config.setMaxWaitMillis(10000);
            pool = new JedisPool(config, host, port);
        }
        return pool.getResource();
    }
    
    public void returnResource(Jedis jedis){
        if(null != jedis){
            pool.returnResourceObject(jedis);
        }
    }
    
    public void setObj(String key , Object map){
        Jedis jedis = null;
        byte[] keys = null;
        byte[] values = null;
        try {
            jedis = getJedis();
            keys = serializer.serialize(key);
            values = serializer.serialize(map);
            jedis.set(keys, values);
        } catch (Exception e) {
            log.error("jedis setMap error", e);
        } finally {
            jedis.close();
        }
    }
    
    public <T> T getObj(String key,Class<T> t){
        Jedis jedis = null;
        byte[] keys = null;
        T map = null;
        try {
            jedis = getJedis();
            keys = serializer.serialize(key);
            byte[] data = jedis.get(keys);
            if(null != data){
                map = serializer.deserialize(data, t);
            }
        } catch (Exception e) {
            log.error("jedis getMap error", e);
        } finally {
            jedis.close();
        }
        return map;
    }
    
}