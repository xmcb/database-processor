package com.juanpi.dbprocessor.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBEventSequence {

    private static Logger log = LoggerFactory.getLogger(DBEventSequence.class);
    
    private static final String ip = getLocalIP();
    
    private static final Long startTime = System.currentTimeMillis();
    
    private static ConcurrentMap<String, Long> eventSequenceMap = new ConcurrentHashMap();
    
    public static synchronized String getEventId(String eventName){
        if(eventSequenceMap.containsKey(eventName)){
            Long id = eventSequenceMap.get(eventName);
            ++id;
            eventSequenceMap.put(eventName, id);
            return ip+ "-" + eventName + ">" + startTime + ">" + id;
        }else{
            eventSequenceMap.put(eventName, 1L);
            Long id = eventSequenceMap.get(eventName);
            return ip+ "-" + eventName + ">" + startTime + ">" + id;
        }
    }
    
    
    public static String getLocalIP(){
        StringBuilder ifconfig = new StringBuilder();  
        try {  
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
                NetworkInterface intf = en.nextElement();  
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
                    InetAddress inetAddress = enumIpAddr.nextElement();  
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {  
                        ifconfig.append(inetAddress.getHostAddress());
                        break;
                    }  
       
                }  
            }  
        } catch (Exception e) {  
            log.error("DBEventSequence.getLocalIP  happend exception", e);
        }  
        return ifconfig.toString();
    }
    
    public static void main(String[] args) {
        System.out.println(getLocalIP());
    }
}
