package com.juanpi.dbprocessor;

import java.io.Serializable;


public class Student implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -8164419486784188228L;

    private String name;
    
    /*private String sex;
    
    private String age;
    
    private String address;
    
    private String phone;*/
    
    public Student(){
    }

    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
    /*public String getSex() {
        return sex;
    }

    
    public void setSex(String sex) {
        this.sex = sex;
    }

    
    public String getAddress() {
        return address;
    }

    
    public void setAddress(String address) {
        this.address = address;
    }

    
    public String getAge() {
        return age;
    }

    
    public void setAge(String age) {
        this.age = age;
    }

    
    public String getPhone() {
        return phone;
    }

    
    public void setPhone(String phone) {
        this.phone = phone;
    }*/
    
    
    
}
