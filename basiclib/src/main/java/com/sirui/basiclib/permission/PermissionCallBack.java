package com.sirui.basiclib.permission;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * author: hewei
 * created on: 2018/3/26 9:35
 * description:权限申请结果接口
 */
public interface PermissionCallBack {

    void success(List<String> permissions);

    void fail(@NonNull List<String> permissions);

}
