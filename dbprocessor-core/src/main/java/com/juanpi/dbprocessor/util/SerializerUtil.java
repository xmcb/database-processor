package com.juanpi.dbprocessor.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component("serialize")
public class SerializerUtil {
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
	public byte[] serialize(Object obj) throws Exception {
	    ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
		try {
		    bos = new ByteArrayOutputStream();
		    oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			return bos.toByteArray();
		} catch (Exception e) {
			throw new Exception("serialize error", e);
		} finally {
		    if(null != oos){
		        oos.close();
		    }
		    if(null != bos){
		        bos.close();
		    }
        }
	}

	public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(data));
			return (T) ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new Exception("class not found when deserialize", e);
		} catch (Exception e2) {
			throw new Exception("deserialize error", e2);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException ex) {
				}
			}
		}
	}
}
