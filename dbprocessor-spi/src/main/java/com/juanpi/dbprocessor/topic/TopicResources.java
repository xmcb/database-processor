package com.juanpi.dbprocessor.topic;

/**
 * dbprocessor刷库topic资源
 * @version 
 * <pre>
 * Author   Version     Date        Changes
 * fu.wan   1.0         2015-9-24   Created
 * </pre>
 * @since 1.
 */
public class TopicResources {

    
    /**
     * 购物车processor刷库topic
     */
    public static final String CART_PROCESSOR_TOPIC = "cart.domain.Cart";
    
    /**
     * 商品processor刷库topic
     */
    public static final String GOODS_PROCESSOR_TOPIC = "goods.domain.Goods";
    
    /**
     * 订单processor刷库topic
     */
    public static final String ORDER_PROCESSOR_TOPIC = "order.domain.Order";
    
    /**
     * 营销processor刷库topic
     */
    public static final String MKT_PROCESSOR_TOPIC = "mkt.domain.Market";
    
    /**
     * 用户processor刷库topic
     */
    public static final String USER_PROCESSOR_TOPIC = "user.domain.User";
 
}
