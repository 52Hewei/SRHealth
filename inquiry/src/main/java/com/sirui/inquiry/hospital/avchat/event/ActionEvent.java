package com.sirui.inquiry.hospital.avchat.event;

/**
 * Created by xiepc on 2017/9/19 10:47
 */

public class ActionEvent {

    private int eventType;
    private int what;

    public ActionEvent() {
    }

    public ActionEvent(int eventType) {
        this.eventType = eventType;
    }

    public ActionEvent setEventType(int eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getEventType() {
        return eventType;
    }

    public Object object;

    public Object getObject() {
        return object;
    }

    public ActionEvent setObject(Object object) {
        this.object = object;
        return this;
    }

    public Object object2;

    public Object getObject2() {
        return object2;
    }

    public ActionEvent setObject2(Object object) {
        this.object2 = object;
        return this;
    }

    public int getWhat() {
        return what;
    }

    public ActionEvent setWhat(int what) {
        this.what = what;
        return this;
    }
}
