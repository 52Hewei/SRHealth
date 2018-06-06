package com.sirui.router.provider;

import android.app.Activity;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * author: hewei
 * created on: 2018/4/8 11:05
 * description:
 */
public interface IMainProvider extends IProvider {
    void goMain(Activity activity);
}
