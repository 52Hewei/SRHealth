package com.sirui.inquiry.hospital.avchat;

import com.netease.nimlib.sdk.avchat.model.AVChatData;

/**
 * Created by xiepc on  2017-08-27 17:07
 */

public interface AVChatStateListener {

    void closeSessions(int exitCode);

    void onCalling(AVChatData data);

    void onUserJoined(String account);

    void onUserLeave(String account, int event);

    void onCallEstablished();

    void onRemoteCamerChange(int stateCode);
}
