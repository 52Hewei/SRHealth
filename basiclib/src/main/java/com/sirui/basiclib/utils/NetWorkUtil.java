package com.sirui.basiclib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by xiepc on 2017/11/16 16:25
 */

public class NetWorkUtil {

    /**
     * 判断是否有网络连接
     * @return
     */
    public static   boolean isNetworkConnected() {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) Utils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        return false;
    }
}
