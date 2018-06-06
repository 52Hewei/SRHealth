package com.sirui.inquiry.hospital.chat.module;

import android.app.Activity;

/**
 *
 * Create by xiepc on 2017/3/17 15:25
 */
public class Container {
    public final Activity activity;
    /**对方帐号*/
    public final String account;
    public final ModuleProxy proxy;

    public Container(Activity activity, String account,ModuleProxy proxy) {
        this.activity = activity;
        this.account = account;
        this.proxy = proxy;
    }
}
