package com.sirui.main.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.net.client.event.EventType;
import com.net.client.event.MainEvent;
import com.sirui.basiclib.utils.ActivityUtils;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.utils.ScreenUtils;
import com.sirui.basiclib.widget.DialogUtil;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.ILoginProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xiepc on 2018/3/29 17:35
 */

public class EventResolver {

    private Context mContext;

    public EventResolver(Context context) {
        mContext = context;
    }

    public void register() {
        EventBus.getDefault().register(this);
    }

    public void unregister() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MainEvent event) {
        switch (event.getEventType()) {
            case EventType.OTHER_LOGIN:
                MyLog.i("--账号在别处登录--");
                showLoginDialog(event);
                break;
            case EventType.HTTP_ERROR:
                MyLog.i("----网络错误---");
                showNetErrorDialog(event);
                break;
        }
    }


    private void showLoginDialog(MainEvent event) {
        DialogUtil.both(ActivityUtils.getTopActivity(), "您的账号已在别处登录！", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.i("屏幕高度：" + ScreenUtils.getScreenHeight());
                //MyToast.show("EventResolver跳到登录");
               ILoginProvider iLoginProvider = (ILoginProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_LOGIN_SERVICE).navigation();
               if(iLoginProvider != null){
                   iLoginProvider.goToLogin((Activity) mContext);
               }
            }
        });
    }

    private void showNetErrorDialog(MainEvent event){
         DialogUtil.confirm(ActivityUtils.getTopActivity(), "连接服务器失败"+event.getMsg(),null);
    }
}
