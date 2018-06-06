package com.sirui.inquiry.hospital.avchat.event;

/**
 * Created by xiepc on 2017/9/1 16:06
 */

public class EventType {

    public static final int LARGE_TO_SMALL_WINDOW = 101;
    public static final int SMALL_TO_LARGE_WINDOW = 102;
    public static final int START_OUT_CALL = 103;
    public static final int START_INCOMING = 104;
    public static final int CLOSE_AVCHAT = 105;
    public static final int UPDATE_AVCHAT_MSG = 106;
    public static final int ENTER_PATIENT_CASE = 107;
    public static final int CANCEL_INQUIRY = 108;
    public static final int OVER_INQUIRY_HAS_PRESCRIPTION = 109;
    public static final int OVER_INQUIRY_NO_PRESCRIPTION = 110;
    /**
     * 证书申请成功
     */
    public static final int CERTIFICATE_APPLY_SUCCESS = 111;
    public static final int MAIN_OVER_INQUIRY = 112;
    /**
     * 重新登录
     */
    public static final int ACTION_TO_LOGIN = 113;
    /**
     * 未读消息提醒
     */
    public static final int MSG_UNREAD = 114;
    /**
     * 关闭病例界面，传病历过来
     */
    public static final int FINISH_CASE_ACTIVITY = 115;
    public static final int SOUND_PLAY_START = 116;
    public static final int SOUND_PLAY_STOP = 117;
    public static final int FINISH_P2PCHAT = 118;
    public static final int PATIENTCANCEL = 119;
    /**
     * 获得公钥
     */
    public static final int GET_PUBLIC_KEY = 120;
    /**
     * 工作平台更新最近联系人列表
     */
    public static final int WORKPLAT_UPDATE_RECENT_MEMBER = 121;
    /**
     * 工作平台更新待接诊列表
     */
    public static final int WORKPLAT_UPDATE_TO_TREAT_LIST = 122;
    /**
     * 通知聊天界面未读消息条数更新
     */
    public static final int P2PCHAT_UPDATE_UNREAD_MSG_NUMBER = 123;
    /**
     * 主页更新待接诊人数
     */
    public static final int UPDATE_HOME_PAGE_INQUIRY_NUM = 124;
}
