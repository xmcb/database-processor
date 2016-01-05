package com.juanpi.dbprocessor.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.juanpi.sdk.ec.DefaultEventListenerFactory;


public class Main {

    private static volatile boolean running = true;
    
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "prod");
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
        applicationContext.start();
        DefaultEventListenerFactory.getEventListener().init();
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                synchronized (Main.class) {
                    running = false;
                    Main.class.notify();
                }
            }
        });

        synchronized (Main.class) {
            while (running) {
                try {
                    Main.class.wait();
                } catch (Throwable e) {
                }
            }
        }
        
    }
    
}
