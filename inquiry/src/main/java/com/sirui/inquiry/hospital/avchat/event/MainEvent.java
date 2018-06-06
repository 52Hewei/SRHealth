package com.sirui.inquiry.hospital.avchat.event;

/**
 * Created by xiepc on 2017/8/31 19:11
 */

public class MainEvent {

    public MainEvent(int state, int what) {
        this.state = state;
        this.what = what;
    }
    public MainEvent(int state) {
        this.state = state;
    }
    private int  state;

    private int what;

    private Object object;


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
