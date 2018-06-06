package com.sirui.inquiry.hospital.util;

import android.view.View;

/**
 *  防止连接切换点击，导致设备卡死
 * Created by xiepc on 2017/3/22 10:59
 */

public class ClickLimitController {

    private boolean clickLimited;
    private View view;

    public ClickLimitController(View view){
        this.view = view;
    }
    /**
     * 执行点击限制操作
     * @param millis 毫秒
     */
    public boolean executeClickLimit(int millis) {
        if (clickLimited) { //如果处于限制状态则不可点击
            return false;
        }
        clickLimited = true;
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                clickLimited = false;
            }
        }, millis);
        return true;
    }

    public boolean executeClickLimit() {
        return executeClickLimit(500);
    }
}
