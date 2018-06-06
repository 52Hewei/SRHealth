package com.sirui.inquiry.hospital.cache;

import android.content.Context;

/**
 * Created by xiepc on 2017/3/30 16:18
 */

public class IMCache {

    private static IMCache instance;

    private String toAccount;

    private String account;
    /**对方昵称*/
    private String toNickName;

    private Context context;

    private IMCache(){}

    public static IMCache getInstance(){
        if(instance == null){
            synchronized (IMCache.class){
                if(instance == null){
                    instance = new IMCache();
                }
            }
        }
        return  instance;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToNickName() {
        return toNickName;
    }

    public void setToNickName(String toNickName) {
        this.toNickName = toNickName;
    }
}
