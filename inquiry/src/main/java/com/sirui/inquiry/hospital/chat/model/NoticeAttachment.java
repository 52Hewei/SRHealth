package com.sirui.inquiry.hospital.chat.model;


import com.sirui.inquiry.hospital.chat.constant.MsgTypeEnum;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiepc on 2017/3/28 18:44
 */

public class NoticeAttachment extends CustomAttachment {

    private String content;

    public NoticeAttachment(){
        super(MsgTypeEnum.NOTICE.getValue());
    }

    @Override
    protected void parseData(JSONObject data) {
        content = data.optString("content");
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content",content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
