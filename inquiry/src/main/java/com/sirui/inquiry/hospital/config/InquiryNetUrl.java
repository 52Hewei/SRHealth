package com.sirui.inquiry.hospital.config;


import com.sirui.basiclib.BuildConfig;

/**
 * Created by xiepc on 2016/12/22 14:23
 */

public class InquiryNetUrl {

    //  public static final String BASE_URL = "Http://192.168.1.132:8088/";
//    public static final String BASE_URL = "http://120.25.203.230:8082/";
//    public static final String URL_GET_CASELIST = BASE_URL + "app/innerService/doctor/caseList";
//    public static final String BASE_URL = "http://uat-yun.siruijk.com:8081/";
//    public static final String BASE = "http://uat-houtai.siruijk.com:8081/";
    public static final String BASE = BuildConfig.API_BASE_URL;

    /**
     * 患者请求排队问诊
     */
//    public static final String URL_REQUEST_QUEUE = BASE_URL + "app/openService/patient/queue";
    public static final String URL_REQUEST_QUEUE = BASE + "app/inner/v1/inquiry/queue";
    /**
     * 取消排队
     */
    public static final String URL_CANCEL_REQUEST_QUEUE = BASE + "app/inner/v1/inquiry/cancelQueue";
    /**
     * 执班医生列表
     */
//    public static final String URL_DOCTOR_LIST = BASE_URL + "app/openService/inquiry/doctor/list";
//    public static final String URL_DOCTOR_LIST = BASE + "app/inner/v1/inquiry/doctor/list";
    public static final String URL_DOCTOR_LIST = BASE + "app/open/v2/inquiry/doctor/list";
    /**
     * 医生详情
     */
    public static final String URL_DOCTOR_DETAIL = BASE + "app/inner/v1/doctor/detail";

    /**
     * 医生详情(登录区外)
     */
    public static final String URL_DOCTOR_DETAIL_OPEN = BASE + "app/open/v1/doctor/detail";

    /**
     * 游客模式登录
     */
    public static final String URL_VISITOR_LOGIN = BASE + "app/open/v1/visitors/login";

    /**
     * 极速问诊
     */
    public static final String URL_INQUIRY_QUICK = BASE + "app/inner/v1/inquiry/quick";

    /**
     * 获取极速问诊二维码
     */
    public static final String URL_INQUIRY_QRCODE = BASE + "app/open/v1/inquiry/qrcode";

    /**
     * 问诊记录列表
     */
    public static final String URL_RECORD_LIST = BASE + "app/inner/v1/inquiry/list";
    /**
     * 问诊记录详情
     */
    public static final String URL_RECORD_DETAIL = BASE + "app/inner/v1/inquiry/detail";
    /**
     * 处方详情接口 URL
     */
    public static final String URL_PRESCRIPTION_DETAIL = BASE + "app/inner/v1/prescription/detail";
    /**
     * 查询当前医生问诊人数
     */
    public static final String URL_QUEUE_NUMBER = BASE + "app/inner/v1/patient/queueNumber";
    /**
     * 恢复问诊单详情
     */
    public static final String URL_INQUIRY_ORDER_DETAIL = BASE + "app/inner/v1/order/patient/detail";
    /**
     * 取消问诊
     */
    public static final String URL_ORDER_CANCEL = BASE + "app/inner/v1/order/cancel";
    /**
     * 获取加密公钥
     */
    public static final String URL_GET_PUBLIC_KEY = BASE + "platform/getPublicKeys";
    /** 登录区外发送短信验证码 */
    public static final String URL_SMS_SEND2 = BASE + "/app/open/v1/sms/send2";

    public static final String CHANGE_DOCTOR = BASE + "app/inner/v1/doctor/changeDoctor";//更换医生
    public static final String INQUIRY_CHECK = BASE + "app/inner/v1/inquiry/inquiryCheck";//查询是否有之前的单

    // 删除问诊记录
    public static final String DEL_INQUIRY_INFO = BASE + "app/inner/v1/user/delInquiryInfo";


}
