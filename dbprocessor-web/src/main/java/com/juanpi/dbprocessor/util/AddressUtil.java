package com.juanpi.dbprocessor.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AddressUtil {

    private static Logger log = LoggerFactory.getLogger(AddressUtil.class); 
    
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
    
}
