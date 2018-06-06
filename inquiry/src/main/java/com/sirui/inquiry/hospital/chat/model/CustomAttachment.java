package com.sirui.inquiry.hospital.chat.model;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import org.json.JSONObject;

/**
 * 自定义消息基类
 * Created by xiepc on 2017/3/28 18:38
 */

public abstract class CustomAttachment implements MsgAttachment {

    // 自定义消息附件的类型，根据该字段区分不同的自定义消息
    protected int type;

    CustomAttachment(int type) {
        this.type = type;
    }

    // 解析附件内容。  接收到的消息解析
    public void fromJson(JSONObject data) {
        if (data != null) {
            parseData(data);
        }
    }

    // 实现 MsgAttachment 的接口，封装公用字段，然后调用子类的封装函数。   发送的消息包装
    @Override
    public String toJson(boolean send) {
        return CustomAttachParser.packData(type, packData());
    }

    // 子类的解析和封装接口。
    protected abstract void parseData(JSONObject data);
    protected abstract JSONObject packData();
}
