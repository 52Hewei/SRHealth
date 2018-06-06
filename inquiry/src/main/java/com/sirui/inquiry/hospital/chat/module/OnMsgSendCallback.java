package com.sirui.inquiry.hospital.chat.module;

/**
 * Created by xiepc on 2017/5/22 11:44
 */

public interface OnMsgSendCallback {
    void onSendSuccess();
    void onSendFailed(int code);
    void onException(Throwable exception);
}
