package com.sirui.inquiry.hospital.chat.constant;

/**
 * Created by xiepc on 2017/3/14 19:42
 */

public enum  SessionTypeEnum {

    P2P(0),
    Team(1);

    private int value;

    SessionTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SessionTypeEnum typeOfValue(int value) {
        for (SessionTypeEnum e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }
        return P2P;
    }
}
