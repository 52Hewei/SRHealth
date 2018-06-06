package com.sirui.router.provider;

import android.app.Activity;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * author: hewei
 * created on: 2018/4/9 15:18
 * description:
 */
public interface IGuideProvider extends IProvider{

    void goGuide(Activity activity);

}
