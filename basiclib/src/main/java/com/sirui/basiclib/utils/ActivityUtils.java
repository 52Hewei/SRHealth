package com.sirui.basiclib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理类
 * Create by xiepc on 2016/12/26 14:06
 */
public class ActivityUtils {

    private final static List<Activity>  activityList = new ArrayList<>();

    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断是否存在Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isActivityExists(Context context, String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return !(context.getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(context.getPackageManager()) == null ||
                context.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }
    /**
     * 获取launcher activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @return launcher activity
     */
    public static String getLauncherActivity(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo info : infos) {
            if (info.activityInfo.packageName.equals(packageName)) {
                return info.activityInfo.name;
            }
        }
        return "no " + packageName;
    }

    public static void addActivity(Activity activity){
        MyLog.i("addActivity--"+activity.getLocalClassName());
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        MyLog.i("removeActivity--"+activity.getLocalClassName());
        activityList.remove(activity);
    }

    public static void finishAllActivity(){
        for (Activity activity: activityList) {
            MyLog.i("finishAllActivity--"+activity.getLocalClassName());
                activity.finish();
        }
    }

    /**
     * 关闭除了当前activity以外的所有
     * @param currentActivity
     */
    public static void finishAllActivityExcept(Activity currentActivity){
        for (Activity activity: activityList) {
            if(!activity.getClass().isInstance(currentActivity)){
                activity.finish();
                MyLog.i("finishAllActivity--"+activity.getLocalClassName());
            }else{
                MyLog.i("当前activity:" + activity.getLocalClassName());
            }
        }
    }



//    public static void finishAllActivityExceptMain(){
//        for (Activity activity: activityList) {
//            if(!(activity instanceof MainActivity)){
//                MyLog.i("finishAllActivity--"+activity.getLocalClassName());
//                activity.finish();
//            }
//        }
//    }

    public static List<Activity> getActivityList() {
        return activityList;
    }


    /**
     * 获得最上层的activity
     * @return
     */
    public static Activity getTopActivity() {
         return   activityList.get(activityList.size()-1);
    }

}
