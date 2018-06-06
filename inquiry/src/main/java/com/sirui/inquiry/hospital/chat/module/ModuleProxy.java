package com.sirui.inquiry.hospital.chat.module;

/**
 * 会话窗口提供给子模块的代理接口。
 * Create by xiepc on 2017/3/17 15:28
 */
public interface ModuleProxy{
    // 发送消息
    void sendTextMessage(String text);

//    void sendNoticeMessage(String text);

//    T createTextMessage(String account,String text);
//
//    // 消息输入区展开时候的处理
//    void onInputPanelExpand();
//
//    // 应当收起输入区
//    void shouldCollapseInputPanel();
//
//    // 是否正在录音
//    boolean isLongClickEnabled();
//    BaseMessage convertTextMessage(T message);
}
