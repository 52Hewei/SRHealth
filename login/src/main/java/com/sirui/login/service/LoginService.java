package com.sirui.login.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.login.LoginActivity;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.ILoginProvider;

/**
 * Created by xiepc on 2018/3/22 16:09
 */
@Route(path = RouterPath.ROUTER_PATH_TO_LOGIN_SERVICE, name = "登陆页面")
public class LoginService implements ILoginProvider {

    @Override
    public void goToLogin(Activity activity) {
        Intent loginIntent = new Intent(activity, LoginActivity.class);
        activity.startActivity(loginIntent);
    }

    @Override
    public void init(Context context) {
        MyLog.i("LoginService--初始化");
    }
}
