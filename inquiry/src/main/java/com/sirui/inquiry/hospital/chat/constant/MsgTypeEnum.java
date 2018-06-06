package com.sirui.inquiry.hospital.chat.constant;

/**
 * Created by xiepc on 2017/3/14 19:37
 */

public enum  MsgTypeEnum {
    /**文字发送*/
    TXT(0),
    /**图片发送*/
    IMG(1),
    /**系统通知*/
    NOTICE(2),

    TIP(3),
    /**视频通话*/
    AVCHAT(4);

    private int value;

    MsgTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
