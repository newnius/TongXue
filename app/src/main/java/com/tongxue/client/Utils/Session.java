package com.tongxue.client.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chaosi on 2015/9/6.
 */
public class Session {

    private Map<String, Object> objectContainer;

    private static Session session;

    private Session(){
        objectContainer = new HashMap<String,Object>();
    }

    public static Session getSession(){
        if(session == null){
            session = new Session();
            return session;
        }else{
            return session;
        }
    }

    public void put(String key, Object value){
        objectContainer.put(key, value);
    }

    public Object get(String key){
        return objectContainer.get(key);
    }

    public void cleanUpSession(){
        objectContainer.clear();
    }

    public void remove(Object key){
        objectContainer.remove(key);
    }
}
