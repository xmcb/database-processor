package com.juanpi.dbprocessor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * where条件映射
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
@Target({ElementType.FIELD})
public @interface Condition {

    /**
     * 列名 
     * @return
     */
    String column();
    
    /**
     * 连接符号   目前只能为（=等于|>大于|<小于|>=大于等于|<=小于等于|!=等于）
     * @return
     */
    Operator symbol() default Operator.custom;
    
}
