package com.sirui.inquiry.hospital.avchat;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;


import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.utils.SRLog;
import com.sirui.basiclib.widget.DialogUtil;
import com.sirui.basiclib.widget.SRToast;

import java.lang.reflect.Method;

/**
 * 视频问诊的超类，主要作用于悬浮窗的权限申请
 * Created by xiepc on 2017/10/13 14:17
 */

public abstract class AVChatBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * 判断悬浮窗权限
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isFloatWindowOpAllowed(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return checkOp(context, 24);  // AppOpsManager.OP_SYSTEM_ALERT_WINDOW
        } else {
            if ((context.getApplicationInfo().flags & 1 << 27) == 1 << 27) {
                return true;
            } else {
                return false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
//                Class<?> spClazz = Class.forName(manager.getClass().getName());
                Method method = manager.getClass().getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int property = (Integer) method.invoke(manager, op,
                        Binder.getCallingUid(), context.getPackageName());
                SRLog.i(" property: " + property);

                if (AppOpsManager.MODE_ALLOWED == property) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            SRLog.i("Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public void checkPermission() {
        if (isFloatWindowOpAllowed(this)) {//已经开启
            switchFloatWindow();
        } else {
            DialogUtil.both(this, "未授予悬浮窗权限，需要您在权限管理中允许该权限，进入设置打开权限？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchPermission();
                }
            });
        }
    }

    /**
     * 打开权限设置界面
     */
    private void openSetting() {
        try {
            Intent localIntent = new Intent(
                    "miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", getPackageName());
            startActivityForResult(localIntent, 11);
            SRLog.i("启动小米悬浮窗设置界面");
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent1.setData(uri);
            startActivityForResult(intent1, 11);
            SRLog.i("启动悬浮窗界面");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SRLog.i("---onActivityResult---requestCode="+requestCode);
        if (requestCode == 11) {
            if (isFloatWindowOpAllowed(this)) {//已经开启
                switchFloatWindow();
            } else {
                SRToast.show("开启悬浮窗失败");
            }
        } else if (requestCode == 12) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    SRToast.show("权限授予失败,无法开启悬浮窗");
                } else {
                    switchFloatWindow();
                }
            }
        }
    }

    protected void switchPermission(){
        if ("Xiaomi".equals(Build.MANUFACTURER)) {//小米手机
            SRLog.i("小米手机");
            openSetting();
        } else if ("Meizu".equals(Build.MANUFACTURER)) {//魅族手机
            SRLog.i("魅族手机");
            openSetting();
        } else {//其他手机
            SRLog.i("其他手机");
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivityForResult(intent, 12);
                } else {
                    switchFloatWindow();
                }
            } else {
                switchFloatWindow();
            }
        }
    }

    protected abstract void switchFloatWindow();
}
