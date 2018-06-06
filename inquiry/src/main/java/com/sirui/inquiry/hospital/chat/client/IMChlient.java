package com.sirui.inquiry.hospital.chat.client;


import com.sirui.inquiry.hospital.chat.model.BaseMessage;

import java.util.List;

/**
 * Created by xiepc on 2017/3/28 11:42
 */

public class IMChlient {
    /**网易云信通讯模块*/
    private IMModule imModule;

    private static IMChlient instance;

    private IMChlient(){}

    public static IMChlient getInstance(){
        if(instance == null ){
            synchronized (IMChlient.class){
                if(instance == null ){
                    instance = new IMChlient();
                }
            }
        }
        return instance;
    }

    /**
     * 注入一个聊天服务模块
     * @param imModule
     */
    public void setClient(IMModule imModule){
        this.imModule = imModule;
    }

    /**
     *
     * @param account   对方账号
     * @param text   文字
     * @param resend   是否为重发消息
     */
    public void sendTextMessage(String account,String text,boolean resend){
        imModule.sendTextMessage(imModule.createTextMessage(account,text),resend);
    }

    public void sendTextMessage(BaseMessage message, boolean resend){
        imModule.sendTextMessage(message,resend);
    }
//
    public void deleteChattingHistory(BaseMessage message){
         imModule.deleteChattingHistory(message);
    }

    public void sendNoticeMessage(String account,String text,boolean resend){
        imModule.sendTextMessage(imModule.createNoticeMessage(account,text),resend);
    }

   public BaseMessage createTextMessage(String account,String text) {
        return imModule.createTextMessage(account,text);
   }

    public void sendMsgReceipt(BaseMessage message){
        imModule.sendMsgReceipt(message);
    }

  /**将接收过来的第三方文字消息转换为项目文字消息*/
    public <T> BaseMessage convertTextMessage(T t) {
           return imModule.convertTextMessage(t);
    }

    public <T> List<BaseMessage> convertBaseMessage(List<T> imMessages){
        return imModule.convertBaseMessage(imMessages);
    }
}
