package com.sirui.router.provider;

import android.app.Activity;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * author: hewei
 * created on: 2018/4/9 17:07
 * description:
 */
public interface IUserGuideProvider extends IProvider{

    void goUserGuide(Activity activity);

}
