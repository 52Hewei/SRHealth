package com.sirui.basiclib.utils;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * author: hewei
 * created on: 2018/4/9 16:24
 * description:
 */
public class RouterJumpUtil {

    @SuppressWarnings("unchecked")
    public static <T> T jumpWithoutData(String routerPath){
        return (T) ARouter.getInstance().build(routerPath).navigation();
    }

}
