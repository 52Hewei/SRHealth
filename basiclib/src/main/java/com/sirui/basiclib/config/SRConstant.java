package com.sirui.basiclib.config;

import com.sirui.basiclib.BuildConfig;

/**
 * Created by xiepc on 2018/3/22 15:53
 */

public class SRConstant {

    public static final boolean DEBUG = BuildConfig.isDebug;
    public static final String TAG = "sirui";

    public static final String SP_NAME = "sp_health";
    /**
     * pad医生端appId
     */
    public static final String APP_ID = BuildConfig.APP_ID;
    /**
     * appSecret
     */
    public static final String APP_SECRET = BuildConfig.APP_SECRET;


    public static final String DATA = "data";
    /**
     * 请求成功 CODE
     */
    public static final String SUCCESS_CODE = "000000";
    /**
     * token失效返回码
     */
    public static final String TOKEN_FAIL_CODE = "000002";

    public static final String ERR_DESC_KEY = "errDesc";
    public static final String ERR_CODE_KEY = "errCode";

    /**
     * 传递web界面的url名称
     */
    public static final String URL_WEB = "url";

    /**
     * 随机数
     */
    public static final String NONCE_KEY = "nonce";
    /**
     * 渠道
     */
    public static final String APP_ID_KEY = "srAppid";
    /**
     * 时间戳
     */
    public static final String TIMESTAMP_KEY = "timestamp";
    /**
     * 哈希摘要
     */
    public static final String SIGNATURE_KEY = "signature";
    /**
     * 接口请求参数最终加密串
     */
    public static final String ENCODE_DATA_KEY = "encodeData";
    /**
     * 敏感信息加密字段
     */
    public static final String ENCODE_FIELDS_KEY = "encodeFields";
    /**
     * 防重放请求
     */
    public static final String REQUEST_ID_KEY = "requestId";

    public static final String PUBLIC_KEY = "public_key";
    public static final String PUBLIC_FILED = "public_filed";

    /**
     * 需要登录APP
     */
    public static final String NEED_LOGIN_APP = "need_login_app";
    /*
    * 启动引导
    * */
    public static final String FIRST_ENTER_GUIDE = "is_first_enter_guide";
    /*
    * 用户引导
    * */
    public static final String FIRST_ENTER_MAIN = "is_first_enter_main";


    public static final String CONTENT_KEY = "content";

    /**
     * SP保存的患者id
     */
    public static final String SP_USER_PHONE = "user_phone";

    public static final String SEX_BOY = "1";
    public static final String SEX_GIRL = "0";

//    public static final String STORE_ID = "4647";
//    public static final String E_ID = "4983";
    public static final String STORE_ID = "926";
    public static final String E_ID = "11";
    /**是否有商城*/
    public static final boolean hasStore = true;

//    public static final String STORE_URL = "https://pvr-yd.siruijk.com/swiMall/quickPurchase";
    public static final String STORE_URL = "https://www.jd.com";

    public static final  boolean  NEED_PAY = true;
    public static final  int  PRICE_VIDEO = 30;
    public static final  int  PRICE_IMAGE_TEXT = 20;

    public static final String WX_PAY_APP_ID = "wx368be98026f3551c";  //wx368be98026f3551c
    public static final String WX_PAY_PARTNER_ID = "1489010462";




}


