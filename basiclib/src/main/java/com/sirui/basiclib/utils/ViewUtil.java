package com.sirui.basiclib.utils;

import android.view.View;
import android.widget.EditText;

/**
 * Created by xiepc on 2016/11/26 17:19
 */

public class ViewUtil {

    /**
     * 指定某视图获取焦点
     */
    public static void requestFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
    }

    /**设置光标到最后*/
    public static void setSelectionEnd(EditText editText){
        editText.setSelection(editText.getText().length());
    }


    /**防止按钮连续点击*/
    public static void setPostEnableClick(final View view){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        },3000);
    }

    /**防止按钮连续点击*/
    public static void setPostEnableClick(final View view, long during ){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        },during);
    }
}
