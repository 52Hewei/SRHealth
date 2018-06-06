package com.sirui.basiclib.permission;

import android.content.Context;
import android.content.DialogInterface;

import com.yanzhenjie.alertdialog.AlertDialog;

/**
 * author: hewei
 * created on: 2018/3/23 17:04
 * description:悬浮窗
 */
public class PermissionDialogUtils {

    /*
    * 申请权限的弹窗
    * */
    public static void permissionDialogShow(Context context , boolean cancelable , int title , String message , int positiveText
            , int negativeText , DialogInterface.OnClickListener positiveListener , DialogInterface.OnClickListener negativeListener){
        AlertDialog.newBuilder(context)
                .setCancelable(cancelable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText , positiveListener)
                .setNegativeButton(negativeText , negativeListener)
                .show();
    }

}
