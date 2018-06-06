package com.sirui.main.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sirui.main.MainActivity;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IMainProvider;

/**
 * author: hewei
 * created on: 2018/4/8 11:07
 * description:
 */
@Route(path = RouterPath.ROUTER_PATH_TO_MAIN_SERVICE, name = "主界面")
public class MainService implements IMainProvider {
    @Override
    public void goMain(Activity activity) {
        Intent mainIntent = new Intent(activity, MainActivity.class);
        activity.startActivity(mainIntent);
    }

    @Override
    public void init(Context context) {
    }
}
