package com.sirui.inquiry.hospital.chat.model;


import com.sirui.basiclib.data.DataManager;
import com.sirui.inquiry.hospital.chat.constant.MsgDirectionEnum;
import com.sirui.inquiry.hospital.chat.constant.MsgStatusEnum;
import com.sirui.inquiry.hospital.chat.constant.MsgTypeEnum;
import com.sirui.inquiry.hospital.chat.constant.SessionTypeEnum;

import org.json.JSONObject;

/**
 * Created by xiepc on 2017/3/14 19:34
 */

public class TextMessage implements BaseMessage {


    private Object bindingMsg;

    private String uuid;

    private MsgDirectionEnum msgDirection;

//    private MsgStatusEnum  msgStatus;

    private MsgTypeEnum msgType;

//    private SessionTypeEnum sessionType;

    private String content;

    private long sendtime;

    private String fromAccount;

    private String toAccount;

    private MsgStatusEnum msgStatus;

    private SessionTypeEnum sessionTypeEnum;

    public TextMessage(){}

    public TextMessage(JSONObject obj){
        this.uuid = obj.optString("id");
        this.sendtime = obj.optLong("msgSendDate");
        this.fromAccount = obj.optString("fromAccount");
        this.toAccount = obj.optString("toAccount");
        this.content = obj.optString("body");
        if("TEXT".equals(obj.optString("msgType"))){
            msgType = MsgTypeEnum.TXT;
        }
         if(DataManager.getInstance().getUser().getImNo().equals(fromAccount)){
             msgDirection = MsgDirectionEnum.Out;
         }else{
             msgDirection = MsgDirectionEnum.In;
         }

    }

    public MsgDirectionEnum getMsgDirection() {
        return msgDirection;
    }

    public void setMsgDirection(MsgDirectionEnum msgDirection) {
        this.msgDirection = msgDirection;
    }

    public MsgTypeEnum getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgTypeEnum msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSendtime() {
        return sendtime;
    }

    public void setSendtime(long sendtime) {
        this.sendtime = sendtime;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public MsgStatusEnum getStatus() {
        return msgStatus;
    }

    @Override
    public void setStatus(MsgStatusEnum status) {
        this.msgStatus = status;
    }

    @Override
    public void setSessionType(SessionTypeEnum status) {

    }

    @Override
    public SessionTypeEnum getSessionType() {
        return null;
    }

    @Override
    public boolean isTheSame(BaseMessage message) {
        return  message.getUuid().equals(uuid);
    }

    @Override
    public Object getBindingMsg() {
        return bindingMsg;
    }

    @Override
    public void setBindingMsg(Object object) {
       this.bindingMsg = object;
    }
}
