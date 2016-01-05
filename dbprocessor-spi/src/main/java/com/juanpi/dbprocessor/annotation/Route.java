package com.juanpi.dbprocessor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由规则ID(标识一条数据的唯一ID，新增修改删除动作时此ID必须相同)
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * fu.wan 	1.0  		2015-8-4 	Created
 *
 * </pre>
 * @since 1.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Route {
    
}
