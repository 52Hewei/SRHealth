package com.sirui.basiclib.widget;

import android.view.Gravity;
import android.widget.Toast;

import com.sirui.basiclib.utils.Utils;

/**
 * @author LuoSiYe
 *         Created on 2017/8/28.
 */

public class SRToast {

    public static void show(String message) {
        show(message,Toast.LENGTH_SHORT);
    }

    public static void show(boolean tip, String message) {
        if (tip) {
            Toast toast = Toast.makeText(Utils.getContext(), message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public static void show(String message,int duration){
        Toast toast = Toast.makeText(Utils.getContext(), message, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
