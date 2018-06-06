package com.sirui.inquiry.hospital.chat.module;


import android.util.Log;

import com.sirui.basiclib.http.JsonUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.inquiry.hospital.chat.P2PChatStateListener;
import com.sirui.inquiry.hospital.chat.client.IMChlient;
import com.sirui.inquiry.hospital.chat.constant.Extras;
import com.sirui.inquiry.hospital.chat.model.BaseMessage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiepc on 2017/3/25 15:15
 */

public class NimProxy implements ModuleProxy {
    /**
     * 对方聊天的帐号
     */
    private String account;

    private MessageProxyCallback messageProxyCallback;

    private P2PChatStateListener stateListener;

    private boolean isStartChat;

    /**
     * 一个时间基准点，早于该时间的消息，不显示
     */
    private long anchorTime;
    /**
     * 过滤后的当前问诊的消息
     */
    private List<IMMessage> filteredMessages = new ArrayList<>();

    public NimProxy(String account, MessageProxyCallback messageProxyCallback, P2PChatStateListener stateListener) {
        this.account = account;
        this.messageProxyCallback = messageProxyCallback;
        this.stateListener = stateListener;
        anchorTime = System.currentTimeMillis() - 3 * 60 * 1000;  //当前时间向后推3分钟之内的消息，认为是本次问诊的消息
    }

    @Override
    public void sendTextMessage(String text) {
        BaseMessage baseMessage = IMChlient.getInstance().createTextMessage(account, text);
        IMChlient.getInstance().sendTextMessage(baseMessage, false);
        messageProxyCallback.onMessageSend(baseMessage);
    }


    /**************************************网易云信消息注册监听部分*****************************************/

    /**
     * 消息状态变化观察者
     */
    Observer<IMMessage> messageStatusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            if (isMyMessage(message)) {
                messageProxyCallback.onMessageStatusChange(IMChlient.getInstance().convertTextMessage(message));
            }
        }
    };

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == SessionTypeEnum.P2P
                && message.getSessionId() != null
                && message.getSessionId().equals(account);
    }

    /**
     * 判断是否有tip消息
     */
    private boolean checkTipMsg(List<IMMessage> messages) {
        for (IMMessage msg : messages) {
            if (msg.getMsgType() == MsgTypeEnum.tip) {
                Log.i(SRConstant.TAG, "tip消息内容为" + msg.getContent());
                String operateType = JsonUtil.getTipMsgOperateType(msg.getContent());
                if (stateListener != null) {
                    if (Extras.TIP_TYPE_OVER_INQUIRY_HAS_PRESCRIPTION.equals(operateType)) { //如果是结束问诊,并且开处方
                        chatFinish(true, msg.getContent());
                    } else if (Extras.TIP_TYPE_OVER_INQUIRY_NO_PRESCRIPTION.equals(operateType)) { //并且不需要开处方
                        chatFinish(false, msg.getContent());
                    } else if (Extras.TIP_TYPE_START_INQUIRY.equals(operateType)) {//如果是开始问诊
                        isStartChat = true;
                        stateListener.onChatStart();
                    } else if (Extras.TIP_TYPE_CANCEL_INQUIRY.equals(operateType)) {//对方取消问诊消息
                        //chatFinish(false,msg.getContent());
                        stateListener.onCancelInquiry();
                    } else if (Extras.TIP_TYPE_REQUEST_HAND_SHAKE.equals(operateType)) {//如果是请求握手消息
                        stateListener.onHandShake(1);
                    } else if (Extras.TIP_TYPE_RESPONSE_HAND_SHAKE.equals(operateType)) {//如果是应答握手消息
                        stateListener.onHandShake(2);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void chatFinish(boolean hasPrescription, String content) {
        JSONObject object = JsonUtil.getTipMsgData(content);
        String diagnose = "";
        String advice = "";
        if (object != null) {
            diagnose = object.optString("diagnose");
            advice = object.optString("advice");
        }
        stateListener.onChatFinish(hasPrescription, diagnose, advice);
    }

//    /**判断是否是video消息或者是视频通话*/
//    private boolean checkVideoMsg(List<IMMessage> messages){
//        for (IMMessage msg: messages) {
//            if(msg.getMsgType() == MsgTypeEnum.video || msg.getMsgType() == MsgTypeEnum.avchat){
//                Log.i(Constants.TAG,"video消息内容为"+msg.getContent());
//                return true;
//            }
//        }
//        return false;
//    }
    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            filteMessages(messages);
            if (filteredMessages == null || filteredMessages.isEmpty() || checkTipMsg(filteredMessages)) { //过滤掉部分不需要解析的消息
                return;
            }
            if (!isStartChat) { //防止由于网络差第一条tip消息丢失的情况
                isStartChat = true;
                Log.i(SRConstant.TAG, "---收到开始问诊消息---");
                stateListener.onChatStart();
            }
            messageProxyCallback.onMessageIncoming(IMChlient.getInstance().convertBaseMessage(filteredMessages)); //处理消息
            messageProxyCallback.sendMsgReceipt(); // 发送已读回执
        }
    };

    /**
     * 过滤掉可能存在之前未接收到的离线消息
     *
     * @param messages 接收到的消息，可能是上次问诊的消息
     * @return 过滤后的本次问诊的消息
     */
    private void filteMessages(List<IMMessage> messages) {
        filteredMessages.clear();
        for (IMMessage msg : messages) {
            if (msg.getTime() > anchorTime && isMyMessage(msg) && msg.getMsgType() != MsgTypeEnum.image && msg.getMsgType() != MsgTypeEnum.avchat) {   //并且是当前聊天对象的消息
                filteredMessages.add(msg);
            } else {
                MyLog.i("过滤离线消息：" + msg.getContent());
            }
        }
    }

    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
        @Override
        public void onEvent(List<MessageReceipt> messageReceipts) {
            /**已读回执接收处理*/
            messageProxyCallback.receiveReceipt();
        }
    };


    public void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeReceiveMessage(incomingMessageObserver, register);  //消息接收监听
        service.observeMessageReceipt(messageReceiptObserver, register);  //已读回执注册监听
    }
}
