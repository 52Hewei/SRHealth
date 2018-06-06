package com.sirui.inquiry.hospital.chat.util;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.inquiry.hospital.chat.module.OnMsgSendCallback;

/**
 * Created by xiepc on 2017/2/23 11:09
 */

public class MessageUtil {

    public static void sendP2PMessage(String sessionId,String content){
        sendP2PMessage(sessionId,content,null);
    }

    public static void sendP2PMessage(String sessionId,String content, final OnMsgSendCallback callback){
        // 创建文本消息
        IMMessage message = MessageBuilder.createTextMessage(
                sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                content // 文本内容
        );
        // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        if(callback != null){
            NIMClient.getService(MsgService.class).sendMessage(message,false).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    callback.onSendSuccess();
                }

                @Override
                public void onFailed(int code) {
                    callback.onSendFailed(code);
                }
                @Override
                public void onException(Throwable exception) {
                    callback.onException(exception);
                }
            });
        }else{
            NIMClient.getService(MsgService.class).sendMessage(message,false);
        }
        MyLog.i("发送消息---->sessionId="+sessionId+"，内容："+content);
    }

     public static void sendTipMessage(String sessionId,String content){
         sendTipMessage(sessionId,content,null);
    }

    public static void sendTipMessage(String sessionId, String content, final OnMsgSendCallback callback){
        IMMessage msg = MessageBuilder.createTipMessage(sessionId, SessionTypeEnum.P2P);
        msg.setContent(content);
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false; //该消息是否允许在消息历史中拉取
        config.enableUnreadCount = false; //该消息是否要计入未读数
        config.enableRoaming = false; //该消息是否需要漫游
        msg.setConfig(config);
        if(callback != null){
            NIMClient.getService(MsgService.class).sendMessage(msg,false).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    callback.onSendSuccess();
                }
                @Override
                public void onFailed(int code) {
                    callback.onSendFailed(code);
                }
                @Override
                public void onException(Throwable exception) {
                    callback.onException(exception);
                }
            });
        }else{
            NIMClient.getService(MsgService.class).sendMessage(msg,false);
        }
        MyLog.i("发送tip消息---->sessionId="+sessionId+"，内容："+content);
    }
}
