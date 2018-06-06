package com.sirui.inquiry.hospital.avchat.event;

/**
 * Created by xiepc on 2017/9/14 16:50
 */

public class UpdateMsgEvent {
    private int eventType;
    public UpdateMsgEvent(int eventType) {
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }
}
