package com.juanpi.dbprocessor.annotation;


public enum Operator {
    /**
     * 等于 =
     */
    eq("等于", "="), 
    
    /**
     * 等于 =
     */
    ne("不等于", "!="),
    
    /**
     * 大于 >
     */
    gt("大于", ">"), 
    
    /**
     * 大于等于 >=
     */
    gte("大于等于", ">="), 
    
    /**
     * 小于 <
     */
    lt("小于", "<"),
    
    /**
     * 小于等于 <=
     */
    lte("小于等于", "<="),
    
    /**
     * 默认 等于=
     */
    custom("默认", "=");
  
    /*isNull("空", "is null"), 
    isNotNull("非空", "is not null"),
    in("包含", "in"), 
    notIn("不包含", "not in"), 
    like("模糊匹配", "like"), 
    notLike("不匹配", "not like"),
    custom("自定义默认的", "=");
    prefixLike("前缀模糊匹配", "like"), 
    prefixNotLike("前缀模糊不匹配", "not like"),
    suffixLike("后缀模糊匹配", "like"), 
    suffixNotLike("后缀模糊不匹配", "not like"),*/
    
    private final String info;
    
    private final String symbol;

    private Operator(final String info, String symbol) {
        this.info = info;
        this.symbol = symbol;
    }
    
    public String getInfo() {
        return info;
    }
    
    public String getSymbol() {
        return symbol;
    }

    



}
