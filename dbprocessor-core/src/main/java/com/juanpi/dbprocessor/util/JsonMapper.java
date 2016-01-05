package com.juanpi.dbprocessor.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by fu.wan on 2014/4/16.
 */
public class JsonMapper {

    /**
     * Object可以是POJO，也可以是Collection或数组。
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    public static String toJson(Object object) {
        return JSON.toJSONString(object,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteNonStringKeyAsString);
    }
    
    /**
     * Object可以是POJO，也可以是Collection或数组。
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    public static String toJsonFormat(Object object){
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue, SerializerFeature.PrettyFormat,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteNonStringKeyAsString);
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。
     * @param obj
     * @return 返回原始json Object对象
     */
    public static Object toJsonObject(Object obj){
    	return JSON.toJSON(obj);
    }
    
    /**
     * 反序列化POJO或简单Collection如List<String>.
     * <p/>
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p/>
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, JavaType)
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        return JSON.parseObject(jsonString, clazz);
    }

    /**
     * 把JSON数据转换成较为复杂的java对象列表
     *
     * @param jsonString JSON字符串
     * @return
     * @throws Exception
     */
    public static <T> T fromJson(String jsonString)
            throws Exception {
        return JSON.parseObject(jsonString,
                new TypeReference<T>() {
                }
        );
    }
}
