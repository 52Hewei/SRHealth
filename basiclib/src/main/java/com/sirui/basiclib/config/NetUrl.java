package com.sirui.basiclib.config;

import com.sirui.basiclib.BuildConfig;

/**
 * Created by xiepc on 2018/3/27 16:09
 */

public class NetUrl {

    public static final String BASE_URL = BuildConfig.API_BASE_URL;

    /**
     * 获取加密公钥
     */
    public static final String URL_GET_PUBLIC_KEY = BASE_URL + "platform/getPublicKeys";

    public static final String VIPURL = BASE_URL + "app/open/v2/patient/login ";// 会员登录

    public static final String VCODE = BASE_URL + "app/open/v1/sms/send2";// 获取验证码

    public static final String UPDATE_USER_INFO = BASE_URL + "app/openService/updateUserInfo";//更新个人信息

    public static final String GET_AGREEMENT_INFO = BASE_URL + "app/open/v1/agreement/getAgreementInfo";// 获取最新协议
    /**
     * 获得升级版本信息
     */
    public static final String URL_GET_VERSION_INFO = BASE_URL + "app/openService/v2/version/getUpdGradeInfo";
     //值班医生列表
    public static final String URL_DOCTOR_LIST = BASE_URL + "app/open/v2/inquiry/doctor/list";
}
