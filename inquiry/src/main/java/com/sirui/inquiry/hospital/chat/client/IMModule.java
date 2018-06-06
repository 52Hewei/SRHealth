package com.sirui.inquiry.hospital.chat.client;


import com.sirui.inquiry.hospital.chat.model.BaseMessage;

import java.util.List;

/**
 * Created by xiepc on 2017/3/28 14:27
 */

public interface IMModule<T> {

    void sendTextMessage(BaseMessage message, boolean resend);

    BaseMessage createTextMessage(String account, String text);

    BaseMessage createNoticeMessage(String account, String text);

    List<BaseMessage> convertBaseMessage(List<T> imMessages);

    BaseMessage convertTextMessage(T imMessage);

    void deleteChattingHistory(BaseMessage message);

    void sendMsgReceipt(BaseMessage message);
}
