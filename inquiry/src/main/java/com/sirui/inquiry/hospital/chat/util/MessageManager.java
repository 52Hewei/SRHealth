package com.sirui.inquiry.hospital.chat.util;


import com.net.client.HttpGo;
import com.net.client.HttpListener;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.inquiry.hospital.chat.model.BaseMessage;
import com.sirui.inquiry.hospital.chat.model.TextMessage;
import com.sirui.inquiry.hospital.config.InquiryNetUrl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiepc on 2017/3/16 14:39
 */

public class MessageManager {

    public static void loadHistroyMessage(String orederNo, final MessageLoadListener listener){
        HttpGo.get(InquiryNetUrl.URL_RECORD_DETAIL)
                .params("orderNo",orederNo)
                .execute(new HttpListener() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject obj = JsonUtil.checkResult(s);
                        if(obj != null && !obj.isNull("data")){
                             JSONObject dataObj = obj.optJSONObject("data");
                             JSONArray jsonArray = dataObj.optJSONArray("imList");
                            if(jsonArray != null && jsonArray.length() > 0){
                                List<BaseMessage> items = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    BaseMessage msg = new TextMessage(jsonArray.optJSONObject(i));
                                    items.add(msg);
                                }
                                listener.onLoad(items);
                            }
                        }
                    }
                });
    }


   public interface MessageLoadListener{
         void onLoad(List<BaseMessage> messages);
    }
}
