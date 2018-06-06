package com.sirui.inquiry.hospital.chat.constant;

/**
 * Created by xiepc on 2017/3/14 19:40
 */

public enum  MsgStatusEnum {

    /**
     * 正在发送中
     */
    sending(0),

    /**
     * 发送成功
     */
    success(1),

    /**
     * 发送失败
     */
    fail(2);


    private int value;

    MsgStatusEnum(int value){
        this.value = value;
    }

    public static MsgStatusEnum statusOfValue(int status) {
        for (MsgStatusEnum e : values()) {
            if (e.getValue() == status) {
                return e;
            }
        }
        return sending;
    }

    public int getValue(){
        return value;
    }
}
