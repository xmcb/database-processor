package com.juanpi.dbprocessor.error;


public class ErrorCode {

    /**
     * event过期错误
     */
    public static final Integer RETURNSON_EVENT_ERROR = 1;
    
    /**
     * 缓存version错误
     */
    public static final Integer CACHE_VERSION_ERROR = 2;
    
    /**
     * 拉取event错误
     */
    public static final Integer PULL_EVENT_ERROR = 3;
    
    /**
     * 反射获取ORM错误
     */
    public static final Integer REFLEX_ORM_ERROR = 4;
    
    /**
     * 执行SQL错误
     */
    public static final Integer EXECUTE_SQL_ERROR = 5;
    
    /**
     * 获取路由线程ID错误
     */
    public static final Integer ROUTE_THREAD_ERROR = 6;
    
    /**
     * 衍生拉取3个版本错误
     */
    public static final Integer EXPAND_EVENT_ERROR = 7;
    
    /**
     * 非法重复版本错误
     */
    public static final Integer ILLEGAL_VERSION_ERROR = 8;
    
    
}
