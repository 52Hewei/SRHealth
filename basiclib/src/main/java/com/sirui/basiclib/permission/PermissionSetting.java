package com.sirui.basiclib.permission;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

/**
 * author: hewei
 * created on: 2018/3/23 16:32
 * description:请求权限
 */
public class PermissionSetting {

    public static void requestPermission(final Activity activity , final PermissionCallBack callBack , String... permissions ){

        AndPermission
                .with(activity)
                .permission(permissions)
                .rationale(new DefaultRationale())
                .onGranted(new Action() {//成功
                    @Override
                    public void onAction(List<String> permissions) {
                        if (callBack != null) {
                            callBack.success(permissions);
                        }
                    }
                })
                .onDenied(new Action() {//失败
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (callBack != null) {
                            callBack.fail(permissions);
                        }
                        if (AndPermission.hasAlwaysDeniedPermission(activity , permissions)){
                            new PermissionDeniedDialog(activity).deniedSetting(permissions);
                        }
                    }
                }).start();

    }

}
