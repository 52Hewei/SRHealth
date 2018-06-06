package com.sirui.inquiry.hospital.chat.module;


import com.sirui.inquiry.hospital.chat.model.BaseMessage;

import java.util.List;

/**
 * Created by xiepc on 2017/3/25 17:38
 */

public interface MessageProxyCallback {
    /**消息发送*/
    void onMessageSend(BaseMessage message);
    /**消息状态改变回调*/
    void onMessageStatusChange(BaseMessage message);
    /**接收回调*/
    void onMessageIncoming(List<BaseMessage> messages);
    /**发送已读回执*/
    void sendMsgReceipt();
    /**已读回执接收*/
    void receiveReceipt();
}
