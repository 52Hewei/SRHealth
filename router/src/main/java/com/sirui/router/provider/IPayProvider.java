package com.sirui.router.provider;

import android.app.Activity;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by chenran3 on 2017/12/8.
 */

public interface IPayProvider extends IProvider {

    void goToPay(Activity activity,Object object);
}
