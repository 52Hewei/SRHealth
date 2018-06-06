package com.sirui.basiclib.utils;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.sirui.basiclib.config.SRConstant;

/**
 * getSharedPreferences保存数据工具类
 * Created by xiepc on 2016/11/18 17:55
 */

public class SPUtil {

    private static SPManager spManager = new SPManager(Utils.getContext(), SRConstant.SP_NAME);

    public static void putString(String key, String name) {
        spManager.putString(key, name);
    }

    public static String getString(String key) {
        return spManager.getString(key);
    }

    public static String getString(String key, String defaultValue) {
        return spManager.getString(key, defaultValue);
    }

    public static void putInt(String key, int name) {
        spManager.putInt(key, name);
    }

    public static int getInt(String key) {
        return spManager.getInt(key);
    }

    public static int getInt(String key, int defaultValue) {
        return spManager.getInt(key, defaultValue);
    }

    public static void putBoolean(String key, boolean name) {
        spManager.putBoolean(key, name);
    }

    public static boolean getBoolean(String key) {
        return spManager.getBoolean(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return spManager.getBoolean(key, defaultValue);
    }

    public static void clearAll() {
        spManager.clear();
    }


    public static <T> T getObject(Class<T> clazz) {
        String key = getKey(clazz);
        String json = getString(key, null);
        MyLog.i("SPUtil.getObject ="+ json);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 保存一个简单的对象，不可保存泛型对象
     * @param object
     */
    public static void putObject(Object object) {
        String key = getKey(object.getClass());
        Gson gson = new Gson();
        String json = gson.toJson(object);
        MyLog.i("SPUtil.putObject ="+ json);
        putString(key, json);
    }

    public static void removeObject(Class<?> clazz){
        remove(getKey(clazz));
    }

    public static String getKey(Class<?> clazz) {
        return clazz.getName();
    }

    public static void remove(String key) {
//        SharedPreferences sp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = sp.edit();
        spManager.remove(key);
    }
}
