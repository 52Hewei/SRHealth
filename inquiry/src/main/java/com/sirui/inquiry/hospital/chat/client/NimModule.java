package com.sirui.inquiry.hospital.chat.client;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.inquiry.hospital.chat.constant.MsgDirectionEnum;
import com.sirui.inquiry.hospital.chat.constant.MsgStatusEnum;
import com.sirui.inquiry.hospital.chat.constant.MsgTypeEnum;
import com.sirui.inquiry.hospital.chat.constant.SessionTypeEnum;
import com.sirui.inquiry.hospital.chat.model.BaseMessage;
import com.sirui.inquiry.hospital.chat.model.NoticeAttachment;
import com.sirui.inquiry.hospital.chat.model.TextMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 云信通讯模块
 * Created by xiepc on 2017/3/28 14:14
 */

public class NimModule implements IMModule<IMMessage>{

    @Override
    public void sendTextMessage(BaseMessage message, boolean resend) {
        if(message.getBindingMsg() instanceof IMMessage){
            NIMClient.getService(MsgService.class).sendMessage((IMMessage) message.getBindingMsg(), resend);
        }else{
            MyLog.i("发送消息服务端类型不一致");
        }
    }

    @Override
    public BaseMessage createTextMessage(String account, String text) {
        IMMessage imMessage = MessageBuilder.createTextMessage(account, com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.P2P, text);
        return convertTextMessage(imMessage);
    }

    @Override
    public BaseMessage createNoticeMessage(String account, String text) {
        NoticeAttachment attachment = new NoticeAttachment();
        attachment.setContent(text);
        IMMessage message = MessageBuilder.createCustomMessage(account, com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.P2P ,attachment.getContent(), attachment
        );
        return convertTextMessage(message);
    }

    @Override
    public List<BaseMessage> convertBaseMessage(List<IMMessage> imMessages) {
        List<BaseMessage> baseMessages = new ArrayList<>();
        for (IMMessage imMessage : imMessages) {
             baseMessages.add(convertTextMessage(imMessage));
        }
        return baseMessages;
    }

    @Override
    public BaseMessage convertTextMessage(IMMessage imMessage) {
        TextMessage textMessage = new TextMessage();
        textMessage.setContent(imMessage.getContent());
        textMessage.setFromAccount(imMessage.getFromAccount());
        if(imMessage.getDirect() == com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum.In){
            textMessage.setMsgDirection(MsgDirectionEnum.In);
        }else{
            textMessage.setMsgDirection(MsgDirectionEnum.Out);
        }
        if(imMessage.getMsgType() == com.netease.nimlib.sdk.msg.constant.MsgTypeEnum.text){
            textMessage.setMsgType(MsgTypeEnum.TXT);
        }else if(imMessage.getMsgType() == com.netease.nimlib.sdk.msg.constant.MsgTypeEnum.custom){ //如果是自定义的消息
            textMessage.setMsgType(MsgTypeEnum.NOTICE);  //目前自定义消息为系统通知
        }else if(imMessage.getMsgType() == com.netease.nimlib.sdk.msg.constant.MsgTypeEnum.avchat){
            textMessage.setMsgType(MsgTypeEnum.AVCHAT); //
        }else{
            textMessage.setMsgType(MsgTypeEnum.TXT); //默认为文字消息
        }
        textMessage.setToAccount(imMessage.getSessionId());
        textMessage.setSendtime(imMessage.getTime());
        textMessage.setUuid(imMessage.getUuid());
        if(imMessage.getStatus() == com.netease.nimlib.sdk.msg.constant.MsgStatusEnum.sending){
            textMessage.setStatus(MsgStatusEnum.sending);
        }else if(imMessage.getStatus() == com.netease.nimlib.sdk.msg.constant.MsgStatusEnum.success){
            textMessage.setStatus(MsgStatusEnum.success);
        }else if(imMessage.getStatus() == com.netease.nimlib.sdk.msg.constant.MsgStatusEnum.fail){
            textMessage.setStatus(MsgStatusEnum.fail);
        }
        if(imMessage.getSessionType() ==  com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.P2P){
            textMessage.setSessionType(SessionTypeEnum.P2P);
        }else{
            textMessage.setSessionType(SessionTypeEnum.Team);
        }
        textMessage.setBindingMsg(imMessage);
        return textMessage;
    }

    @Override
    public void deleteChattingHistory(BaseMessage baseMessage) {
         if(baseMessage.getBindingMsg() instanceof IMMessage){
             NIMClient.getService(MsgService.class).deleteChattingHistory((IMMessage) baseMessage.getBindingMsg());
         }else{
             MyLog.i("删除消息服务端类型不一致");
         }
    }

    @Override
    public void sendMsgReceipt(BaseMessage message) {
        if(message.getBindingMsg() instanceof IMMessage){
            NIMClient.getService(MsgService.class).sendMessageReceipt(message.getFromAccount(),(IMMessage) message.getBindingMsg());
        }else{
            MyLog.i("已读回执消息服务端类型不一致");
        }
    }
}
