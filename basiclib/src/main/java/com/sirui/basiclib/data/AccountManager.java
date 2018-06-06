package com.sirui.basiclib.data;

import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.utils.SPUtil;

/**
 * 对药店帐号和会员账号的退出登录管理
 * Created by xiepc on 2016/12/7 14:13
 */

public class AccountManager {

    private static AccountManager instance;
//
//    public static final String USER_ACCOUNT_ID = "user_account_id";
//    public static final String USER_CHAT_ID = "user_chat_id";
//    public static final String USER_CHAT_TOKEN = "user_chat_token";


    private AccountManager(){}

    public static AccountManager getInstance(){
        if(instance == null){
            synchronized (AccountManager.class){
                if(instance == null){
                    instance = new AccountManager();
                }
            }
        }
        return  instance;
    }

    /**会员退出登录*/
    public void loginOutMember(){
        DataManager.getInstance().setUser(null);
        SPUtil.putString(SRConstant.SP_USER_PHONE,"");
    }

//    /**清除帐号信息*/
//    public void clearAccountData(){
//        SPUtil.putString(USER_ACCOUNT_ID,"");
//        SPUtil.putString(USER_CHAT_ID,"");
//        SPUtil.putString(USER_CHAT_TOKEN,"");
//    }
}
