package com.sirui.inquiry.hospital.chat.client;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * Created by xiepc on 2017/9/14 16:20
 */

public class NimHistoryMsgManager {

    private static NimHistoryMsgManager instance;

    public static NimHistoryMsgManager getInstance() {
        if (instance == null) {
            synchronized (NimHistoryMsgManager.class) {
                if (instance == null) {
                    instance = new NimHistoryMsgManager();
                }
            }
        }
        return instance;
    }

    public void qureyMessageByUuid(List<String> uuids, final onQueryMessageCallback callback){
          if(uuids == null || uuids.size() == 0){
              return;
          }
         NIMClient.getService(MsgService.class).queryMessageListByUuid(uuids).setCallback(new RequestCallback<List<IMMessage>>() {
             @Override
             public void onSuccess(List<IMMessage> param) {
                  callback.onQueryListener(param);
             }

             @Override
             public void onFailed(int code) {
                  callback.onFailed(code);
             }

             @Override
             public void onException(Throwable exception) {
                 callback.onFailed(0);
             }
         });
    }

    public interface onQueryMessageCallback{
         void onQueryListener(List<IMMessage> param);
         void onFailed(int code);
    }
}
