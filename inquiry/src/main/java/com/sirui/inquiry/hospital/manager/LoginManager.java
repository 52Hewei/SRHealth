package com.sirui.inquiry.hospital.manager;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.widget.MyToast;
import com.sirui.inquiry.hospital.cache.IMCache;
import com.sirui.inquiry.hospital.chat.client.IMChlient;
import com.sirui.inquiry.hospital.chat.client.NimModule;

/**
 * 聊天帐号登录管理
 * * Created by xiepc on 2017/2/8 13:50
 */

public class LoginManager {

    public void login(final String name, final String token, final LoginCallback loginCallback) {
        // final String name = accountEdit.getText().toString().toLowerCase();
//        final String token = tokenFromPassword(passwrod);
        MyLog.i("name=====" + name);
        MyLog.i("token=====" + token);
        IMChlient.getInstance().setClient(new NimModule()); //注入一个网易云信的消息通讯模块
        LoginInfo info = new LoginInfo(name, token); // config...
        RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                IMCache.getInstance().setAccount(name);
                saveLoginInfo(name, token);
                // DoctorSelectedActivity.start(context);//登录成功调转相关界面
                MyLog.i( "登录成功");
                // finish();
                if (loginCallback != null) {
                    loginCallback.onSucess(param);
                }
            }

            @Override
            public void onFailed(int code) {
                MyLog.i("code--" + code);
//                DialogUtil.confirm("登录失败: " + code, null);
                MyToast.show("登录失败: " + code);
                if (loginCallback != null) {
                    loginCallback.onFailed(code);
                }
            }

            @Override
            public void onException(Throwable exception) {
            }
        };
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);
    }

    private void saveLoginInfo(final String account, final String token) {
//        AccountManager.getInstance().setUserChatId(account);
//        AccountManager.getInstance().setUserChatToken(token);
    }


//    private String tokenFromPassword(String password) {
//        String appKey = readAppKey(context);
////        boolean isDemo = "b7ed6e35f4b7350907da34b02d20f4b5".equals(appKey)
//        boolean isDemo = "b7ed6e35f4b7350907da34b02d20f4b5".equals(appKey)
////                || "fe416640c8e8a72734219e1847ad2547".equals(appKey)
//                ;
//        return isDemo ? MD5.getStringMD5(password) : password;
//    }

//    private static String readAppKey(Context context) {
//        try {
//            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//            if (appInfo != null) {
//                return appInfo.metaData.getString("com.netease.nim.appKey");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 当前帐号退出登录
     */
    public void loginOut() {
        NIMClient.getService(AuthService.class).logout(); //退出登录
        MyLog.i("---IM退出登录----");
    }

    public interface LoginCallback {
        void onSucess(LoginInfo param);

        void onFailed(int code);
    }

//    public void setLoginListener(LoginCallback loginCallback){
//        this.loginCallback = loginCallback;
//    }
}
