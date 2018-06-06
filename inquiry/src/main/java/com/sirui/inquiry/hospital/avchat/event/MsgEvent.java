package com.sirui.inquiry.hospital.avchat.event;

/**
 * Created by xiepc on 2017/10/16 11:31
 */

public class MsgEvent {

    private int eventType;

    private Object obj;

    public MsgEvent(int eventType){
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
