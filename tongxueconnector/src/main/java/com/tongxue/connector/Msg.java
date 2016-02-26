package com.tongxue.connector;

/**
 *
 * @author Newnius
 */
public class Msg {
    private final int code;
    private Object obj;

    public Msg(int code, Object obj) {
        this.code = code;
        this.obj = obj;
    }

    public int getCode() {
        return code;
    }

    public Object getObj() {
        return obj;
    }
    
    public void setObj(Object obj){
    	this.obj = obj;
    }
    


    
    
    
}
