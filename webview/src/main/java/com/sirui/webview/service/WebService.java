package com.sirui.webview.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IWebProvider;
import com.sirui.webview.WebActivity;

/**
 * author: hewei
 * created on: 2018/4/4 15:12
 * description:
 */
@Route(path = RouterPath.ROUTER_PATH_TO_WEB_SERVICE, name = "商城页面")
public class WebService implements IWebProvider{
    @Override
    public void goToWeb(Activity activity,String url) {
        Intent intent = new Intent(activity , WebActivity.class);
        intent.putExtra(SRConstant.URL_WEB,url);
        activity.startActivity(intent);
    }

    @Override
    public void init(Context context) {
        MyLog.i("WebService--初始化");
    }
}
