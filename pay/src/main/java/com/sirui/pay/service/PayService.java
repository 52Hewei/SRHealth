package com.sirui.pay.service;

import android.app.Activity;
import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sirui.basiclib.data.bean.PayBean;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.pay.PayActivity;
import com.sirui.router.RouterPath;
import com.sirui.router.provider.IPayProvider;

/**
 * Created by xiepc on 2018/3/22 16:09
 */
@Route(path = RouterPath.ROUTER_PATH_TO_PAY_SERVICE, name = "支付页面")
public class PayService implements IPayProvider {

    @Override
    public void init(Context context) {
        MyLog.i("PayService--初始化");
    }

    @Override
    public void goToPay(Activity activity,Object object) {
         PayActivity.start(activity, (PayBean) object);
    }
}
