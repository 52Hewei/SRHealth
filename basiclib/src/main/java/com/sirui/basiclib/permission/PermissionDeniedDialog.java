package com.sirui.basiclib.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.sirui.basiclib.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.SettingService;

import java.util.List;

/**
 * author: hewei
 * created on: 2018/3/26 9:47
 * description:权限被永久拒绝的提示
 */
public class PermissionDeniedDialog {

    private Context mContext;

    public PermissionDeniedDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void deniedSetting(List<String> permissions){

        List<String> permissionNames = Permission.transformText(mContext, permissions);
        String message = mContext.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));
        final SettingService settingService = AndPermission.permissionSetting(mContext);
        PermissionDialogUtils.permissionDialogShow(mContext, true, R.string.title_dialog, message, R.string.setting, R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settingService.execute();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settingService.cancel();
            }
        });

    }

}
