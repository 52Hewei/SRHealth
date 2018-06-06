package com.sirui.inquiry.hospital.chat.model;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自定义消息解析器
 * Created by xiepc on 2017/3/28 18:40
 */

public class CustomAttachParser implements MsgAttachmentParser {

    @Override
    public MsgAttachment parse(String json) {
        CustomAttachment attachment = null;
        try {
            JSONObject object = new JSONObject(json);
            int type = object.optInt("type");
            JSONObject data = object.getJSONObject("data");
            switch (type) {
                case 2:  //MsgTypeEnum.NOTICE.getValue()
                    attachment = new NoticeAttachment();
                    break;

            }
            if (attachment != null) {
                attachment.fromJson(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attachment;
    }


    public static String packData(int type, JSONObject data) {
        try {
            JSONObject object = new JSONObject();
            object.put("type", type);
            if (data != null) {
                object.put("data", data);
            }
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return "";
    }
}
