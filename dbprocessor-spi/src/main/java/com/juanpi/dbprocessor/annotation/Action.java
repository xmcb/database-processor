package com.juanpi.dbprocessor.annotation;


public enum Action {
    
    /**
     * 修改
     */
    update("修改","update"),
    /**
     * 新增
     */
    insert("新增","insert"),
    /**
     * 删除
     */
    delete("删除","delete");
    
    private final String info;
    
    private final String symbol;
    
    private Action(String info,String symbol){
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
