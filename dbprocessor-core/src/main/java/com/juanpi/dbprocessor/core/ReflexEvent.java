package com.juanpi.dbprocessor.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.juanpi.dbprocessor.annotation.Action;
import com.juanpi.dbprocessor.annotation.Column;
import com.juanpi.dbprocessor.annotation.Condition;
import com.juanpi.dbprocessor.annotation.Route;
import com.juanpi.dbprocessor.annotation.Table;
import com.juanpi.dbprocessor.util.JsonMapper;

@Component("reflexEvent")
public class ReflexEvent {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${dbprocessor.threads}")
    private int threads;
    
    /**
     * 获取线程ID
     * @param obj
     * @return
     */
    public Integer getThreadId(Object obj){
        try {
            Class c = obj.getClass();
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if(null != value){
                    Annotation[] annots  = field.getDeclaredAnnotations();
                    for (Annotation annotation : annots) {
                        if(annotation instanceof Route){
                            int hashCode = value.hashCode();
                            return Math.abs(hashCode % threads);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("ReflexEvent.getThreadId exception:" + JsonMapper.toJson(obj), e);
        }
        return -1;
    }
    
    public Map<String,List<Object>> reflex(Object obj) {
        LinkedHashMap<Column, Object> columnMap = new LinkedHashMap<Column, Object>();
        LinkedHashMap<Condition, Object> conditionMap = new LinkedHashMap<Condition, Object>();
        String actionSymbol = null;
        String tableName = null;
        try {
            Class c = obj.getClass();
            Table table =  (Table)c.getAnnotation(Table.class);
            tableName = table.value();
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                Object value = f.get(obj);
                if(null != value){
                    if (value instanceof Action) {
                        Action action = (Action) value;
                        actionSymbol = action.getSymbol();
                        continue;
                    }
                    Annotation[] annots = f.getDeclaredAnnotations();
                    for (Annotation annot : annots) {
                        if(annot instanceof Column) {
                            Column column = (Column) annot;
                            columnMap.put(column, value);
                        }
                        if (annot instanceof Condition) {
                            Condition condition = (Condition) annot;
                            conditionMap.put(condition, value);
                            continue;
                        }
                        if(annot instanceof Table){
                            tableName = (String)value;
                        }
                    }
                }
            }
            return getSql(actionSymbol, tableName, columnMap, conditionMap);
        } catch (Exception e) {
            log.error("ReflexEvent sql for " + obj.getClass().getName() + "error", e);
        }
        return null;
    }

    private Map<String,List<Object>> getSql(String actionSymbol, String tableName, LinkedHashMap<Column, Object> columnMap,
                         LinkedHashMap<Condition, Object> conditionMap) {
        Map<String,List<Object>> sqlParameMap = new HashMap<String,List<Object>>();
        
        StringBuffer whereStr = new StringBuffer();
        List<Object> whereValuesList = new ArrayList<Object>();
        if(conditionMap.size() > 0){
            for(Iterator<Condition> iterator = conditionMap.keySet().iterator();iterator.hasNext();){
                Condition condition = iterator.next();
                Object value = conditionMap.get(condition);
                whereStr.append(iterator.hasNext()?condition.column()+"=? and ":condition.column()+"=?");
                whereValuesList.add(value);
            }
        }
        
        switch (actionSymbol) {
            case "insert":
                StringBuffer columnStr = new StringBuffer();
                StringBuffer valuesStr = new StringBuffer();
                List<Object> valuesList = new ArrayList<Object>();
                for(Iterator<Column> iterator = columnMap.keySet().iterator();iterator.hasNext();){
                    Column column = iterator.next();
                    Object value = columnMap.get(column);
                    valuesList.add(value);
                    columnStr.append(iterator.hasNext()?column.name()+",":column.name());
                    valuesStr.append(iterator.hasNext()?"?,":"?");
                }
                String insertSql = "insert into " + tableName + "("+ columnStr + ") values(" + valuesStr + ")";
                sqlParameMap.put(insertSql, valuesList);
                break;
            case "update":
                StringBuffer updateColumnStr = new StringBuffer();
                List<Object> updateValuesList = new ArrayList<Object>();
                for(Iterator<Column> iterator = columnMap.keySet().iterator();iterator.hasNext();){
                    Column column = iterator.next();
                    Object value = columnMap.get(column);
                    if(column.autoOper() && value instanceof Integer){
                        Integer intValue = (Integer)value;
                        if(intValue < 0){
                            value = Math.abs(intValue);
                            updateColumnStr.append(iterator.hasNext()?column.name()+"="+column.name()+"-?,":column.name()+"="+column.name()+"-?");
                        }else{
                            updateColumnStr.append(iterator.hasNext()?column.name()+"="+column.name()+"+?,":column.name()+"="+column.name()+"+?");
                        }
                    }else{
                        updateColumnStr.append(iterator.hasNext()?column.name()+"=?,":column.name()+"=?");
                    }
                    updateValuesList.add(value);
                }
                updateValuesList.addAll(whereValuesList);
                String updateSql = "update " + tableName + " set " + updateColumnStr + " where " + whereStr;
                sqlParameMap.put(updateSql, updateValuesList);
                break;
            case "delete":
                String deleteSql = "delete from " + tableName + " where " + whereStr;
                sqlParameMap.put(deleteSql, whereValuesList);
                break;
            default :
                break;
        }
        return sqlParameMap;
    }
}
