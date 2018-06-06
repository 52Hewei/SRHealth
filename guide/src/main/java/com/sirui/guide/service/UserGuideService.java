package com.sirui.guide.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sirui.guide.UserGuideActivity;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IUserGuideProvider;

/**
 * author: hewei
 * created on: 2018/4/9 17:10
 * description:
 */
@Route(path = RouterPath.ROUTER_PATH_TO_USERGUIDE_SERVICE , name = "用户引导页面")
public class UserGuideService implements IUserGuideProvider{
    @Override
    public void goUserGuide(Activity activity) {
        Intent intent = new Intent(activity , UserGuideActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void init(Context context) {
    }
}
