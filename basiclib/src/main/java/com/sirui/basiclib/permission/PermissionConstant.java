package com.sirui.basiclib.permission;

/**
 * author: hewei
 * created on: 2018/3/26 10:12
 * description:权限申请常量
 */
public class PermissionConstant {

    //日历
    public static final String[] CALENDAR = new String[]{"android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR"};
    //相机
    public static final String[] CAMERA = new String[]{"android.permission.CAMERA"};
    //联系人
    public static final String[] CONTACTS = new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.GET_ACCOUNTS"};
    //位置信息
    public static final String[] LOCATION = new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    //麦克风
    public static final String[] MICROPHONE = new String[]{"android.permission.RECORD_AUDIO"};
    //电话
    public static final String[] PHONE = new String[]{"android.permission.READ_PHONE_STATE", "android.permission.CALL_PHONE", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "com.android.voicemail.permission.ADD_VOICEMAIL", "android.permission.USE_SIP", "android.permission.PROCESS_OUTGOING_CALLS"};
    //传感器
    public static final String[] SENSORS = new String[]{"android.permission.BODY_SENSORS"};
    //短信
    public static final String[] SMS = new String[]{"android.permission.SEND_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_SMS", "android.permission.RECEIVE_WAP_PUSH", "android.permission.RECEIVE_MMS"};
    //存储
    public static final String[] STORAGE = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

}
