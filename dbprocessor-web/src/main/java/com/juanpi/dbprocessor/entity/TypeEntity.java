package com.juanpi.dbprocessor.entity;


public class TypeEntity {

    private String name;
    
    private int state;

    
    public TypeEntity(){
    }

    public TypeEntity(String name, int state){
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }

    
}
