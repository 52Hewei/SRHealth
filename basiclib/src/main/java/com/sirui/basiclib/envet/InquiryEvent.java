package com.sirui.basiclib.envet;

/**
 * Created by xiepc on 2017/8/31 19:11
 */

public class InquiryEvent {

    private int type;

    private Object object;

    public InquiryEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
