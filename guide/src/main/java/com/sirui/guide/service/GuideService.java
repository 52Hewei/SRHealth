package com.sirui.guide.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sirui.guide.GuideActivity;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IGuideProvider;

/**
 * author: hewei
 * created on: 2018/4/9 15:22
 * description:
 */
@Route(path = RouterPath.ROUTER_PATH_TO_GUIDE_SERVICE , name = "引导页面")
public class GuideService implements IGuideProvider{
    @Override
    public void goGuide(Activity activity) {
        Intent intent = new Intent(activity , GuideActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void init(Context context) {
    }
}
