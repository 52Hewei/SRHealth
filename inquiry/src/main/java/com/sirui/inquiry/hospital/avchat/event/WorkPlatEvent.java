package com.sirui.inquiry.hospital.avchat.event;

/**
 * Created by xiepc on 2017/12/28 18:43
 */

public class WorkPlatEvent {

    private int eventType;
    private Object obj;

    public WorkPlatEvent(int eventType) {
        this.eventType = eventType;
    }

    public WorkPlatEvent(int eventType, Object obj) {
        this.eventType = eventType;
        this.obj = obj;
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
