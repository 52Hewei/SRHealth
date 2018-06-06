package com.sirui.basiclib.utils;

import android.util.Log;

import com.sirui.basiclib.config.SRConstant;

/**
 * @author LuoSiYe
 *         Created on 2017/8/28.
 */

public class MyLog {

    public static void e(String tag,String message){
        if (SRConstant.DEBUG)
            Log.e(tag,message);
    }

    public static void e(String message){
        if (SRConstant.DEBUG)
            e(SRConstant.TAG,message);
    }

    public static void i(String message){
        if (SRConstant.DEBUG)
            i(SRConstant.TAG,message);
    }
    public static void i(String tag,String message){
        if (SRConstant.DEBUG)
            Log.i(tag,message);
    }

    public static void d(String tag,String message){
        if (SRConstant.DEBUG)
            Log.d(tag,message);
    }
    public static void d(String message){
        if (SRConstant.DEBUG)
            d(SRConstant.TAG,message);
    }

    public static void w(String message){
        if (SRConstant.DEBUG)
            Log.w(SRConstant.TAG,message);
    }
    public static void w(String tag,String message){
        if (SRConstant.DEBUG)
            Log.w(tag,message);
    }

    public static void v(String tag,String message){
        if (SRConstant.DEBUG)
            Log.v(tag,message);
    }

}
