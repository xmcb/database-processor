package com.juanpi.dbprocessor;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.juanpi.dbprocessor.util.SerializerUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisAPI {
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    private SerializerUtil serializer = new SerializerUtil();
    
    private JedisPool pool;
    
    private String host = "192.168.143.135";
    
    private int port = 6379;
    
    private Jedis getJedis() {
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
    
    
    public static void main(String[] args) {
        RedisAPI redis = new RedisAPI();
        /*
        Student student = new Student();
        student.setName("张三");
        student.setAge("20");
        student.setSex("男生");
        redis.setObj("student", student);*/
        
        Student stu = redis.getObj("student", Student.class);
        
        System.out.println(stu.getName());
        
    }
    
}