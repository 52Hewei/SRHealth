package com.sirui.inquiry.hospital.chat.model;


import com.sirui.inquiry.hospital.chat.constant.MsgDirectionEnum;
import com.sirui.inquiry.hospital.chat.constant.MsgStatusEnum;
import com.sirui.inquiry.hospital.chat.constant.MsgTypeEnum;
import com.sirui.inquiry.hospital.chat.constant.SessionTypeEnum;

import java.io.Serializable;

/**
 * Created by xiepc on 2017/3/14 19:24
 */

public interface BaseMessage extends Serializable {

    MsgDirectionEnum getMsgDirection();

    void setMsgDirection(MsgDirectionEnum msgDirection);

    MsgTypeEnum getMsgType();

    void setMsgType(MsgTypeEnum msgType) ;

    String getContent();

    void setContent(String content);

    long getSendtime();

    void setSendtime(long sendtime);

    String getFromAccount();

    void setFromAccount(String fromAccount);

    String getToAccount();

    void setToAccount(String toAccount);
    String getUuid();

    void setUuid(String uuid);

    /**
     * 获取消息接收/发送状态。
     *
     * @return 消息状态
     */
    MsgStatusEnum getStatus();

    /**
     * 设置消息状态
     *
     * @param status 消息状态
     */
    void setStatus(MsgStatusEnum status);

    void setSessionType(SessionTypeEnum status);

    SessionTypeEnum getSessionType();
    /**
     * 判断与参数message是否是同一条消息。<br>
     * 先比较数据库记录ID，如果没有数据库记录ID，这比较{@link #getUuid()}
     *
     * @param message 消息对象
     * @return 两条消息是否相同
     */
    boolean isTheSame(BaseMessage message);

    Object getBindingMsg();

    void setBindingMsg(Object object);
}
