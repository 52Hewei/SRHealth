package com.sirui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.utils.SPUtil;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IGuideProvider;
import com.sirui.router.provider.ILoginProvider;
import com.sirui.router.provider.IUserGuideProvider;

public class BootActivity extends BaseActivity {

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1://需要登录
                    if(SPUtil.getBoolean(SRConstant.FIRST_ENTER_GUIDE,true)){
                        enterGuide();
                    }else {
                        enterLogin();
                    }
                    break;
                case 2:
                    if (SPUtil.getBoolean(SRConstant.FIRST_ENTER_MAIN, true)){
                        enterUserGuide();
                    }else {
                        enterMain();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) { //防止某些手机，应用退回后台点击桌面图标会走启动页
            finish();
            return;
        }
    }

    private void enterUserGuide() {
        IUserGuideProvider guideService = (IUserGuideProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_USERGUIDE_SERVICE).navigation();
        if(guideService != null){
            guideService.goUserGuide(this);
        }
        finish();
    }

    private void enterGuide() {
        IGuideProvider guideService = (IGuideProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_GUIDE_SERVICE).navigation();
        if(guideService != null){
            guideService.goGuide(this);
        }
        finish();
    }

    private void enterLogin() {
        ILoginProvider loginService = (ILoginProvider) ARouter.getInstance().build(RouterPath.ROUTER_PATH_TO_LOGIN_SERVICE).navigation();
        if(loginService != null){
            loginService.goToLogin(this);
        }
        finish();
    }

    private void enterMain() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_boot;
    }

    @Override
    protected void iniData() {
        boolean isFirst = SPUtil.getBoolean(SRConstant.NEED_LOGIN_APP, true);
        Message message = new Message();
        message.what = 1;
//        if (isFirst) {
//            message.what = 1;
//        }else{
//            User user = SPUtil.getObject(User.class);
//            if(user != null){
//                DataManager.getInstance().setUser(user);
//                message.what = 2;
//            }else{
//                message.what = 1;
//            }
//        }
        mHandler.sendMessageDelayed(message,2000);
    }

    @Override
    protected void iniView() {
    }
}
