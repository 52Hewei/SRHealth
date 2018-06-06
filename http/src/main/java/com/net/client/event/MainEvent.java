package com.net.client.event;

/**
 * Created by xiepc on 2017/10/16 11:31
 */

public class MainEvent {

    private int eventType;

    private int code;

    private String msg;

    private Object obj;

    public MainEvent(int eventType){
        this.eventType = eventType;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
