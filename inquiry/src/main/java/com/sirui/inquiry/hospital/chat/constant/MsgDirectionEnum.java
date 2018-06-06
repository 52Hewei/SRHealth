package com.sirui.inquiry.hospital.chat.constant;

/**
 * Created by xiepc on 2017/3/14 19:20
 */

public enum MsgDirectionEnum {
    /**
     * 发出去的消息
     */
    Out(0),
    /**
     * 接收到的消息
     */
    In(1);

    private int value;

    MsgDirectionEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MsgDirectionEnum directionOfValue(int value) {
        for (MsgDirectionEnum direction : values()){
            if (direction.getValue() == value) {
                return direction;
            }
        }
        return Out;
    }
}
