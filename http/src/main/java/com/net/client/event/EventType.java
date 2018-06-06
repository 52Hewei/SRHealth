package com.net.client.event;

/**
 * Created by xiepc on 2018/3/29 17:49
 */

public class EventType {

    /**其它端登录*/
    public final static int OTHER_LOGIN = 10;
    /**token失效*/
    public final static int TOKEN_INVALID = 11;
    /**网络错误*/
    public final static int HTTP_ERROR = 12;
    /*登录成功 */
    public static final int LOGIN_SUCCESS = 13;
    /*全屏广告页面点击隐藏*/
    public static final int SKIP_AD = 14;
}
