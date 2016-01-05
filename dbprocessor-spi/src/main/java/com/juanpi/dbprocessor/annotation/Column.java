package com.juanpi.dbprocessor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表结构映射
 * name 列名
 * type 列类型
 * autoOper 当列为int类型并且update该条数据时，是否允许列以自己为基准进行增减，
 *          如update table set sku=sku+10 (正数为+)
 *          或者update table set sku=sku-10 (负数为-)
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * fu.wan 	1.0  		2015-7-28 	Created
 *
 * </pre>
 * @since 1.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER,ElementType.LOCAL_VARIABLE})
public @interface Column {

    /**
     * 列名
     * @return
     */
    String name();
    
    /**
     * 列类型
     * @return
     */
    String type();
    
    /**
     * autoOper 当列为int类型并且update该条数据时，是否允许列以自己为基准进行增减，
     * 如update table set sku=sku+10 (正数为+)
     * 或者update table set sku=sku-10 (负数为-)
     * @return false 默认不允许自增减 / true 允许自增减
     */
    boolean autoOper() default false;
    
}
