package com.sirui.inquiry.hospital.avchat;

import android.content.Context;

import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.sirui.basiclib.utils.SRLog;

/**
 * Created by xiepc on  2017-08-27 18:30
 */

public class AVChatClientManager {

    private static AVChatClientManager instance;


    private AVChatClientManager() {
    }

    public static AVChatClientManager getInstance() {
        if (instance == null) {
            synchronized (AVChatClientManager.class) {
                if (instance == null) {
                    instance = new AVChatClientManager();
                }
            }
        }
        return instance;
    }

    private AVChatClient avChatClient;
    /**
     * 对方帐号
     */
    private String toAccount;

    public void initAVChat(Context context) {
        if (avChatClient == null) {
            avChatClient = new AVChatClient(context);
            avChatClient.enableAVChat(true);
        }
    }

    public void outGoingAVChat(String toAccount) {
        if (avChatClient != null) {
            avChatClient.outGoingAVChat(toAccount);
        } else {
            SRLog.i("---outGoingAVChat---未初始化AVChatLaunchManager---");
        }
    }

    public void incomingAVChat(AVChatData data) {
        if (avChatClient != null) {
            avChatClient.incomingAVChat(data);
        } else {
            SRLog.i("--incomingAVChat---未初始化AVChatLaunchManager---");
        }
    }

    public void closeAVChat() {
        if (avChatClient != null) {
            if (avChatClient.isCallEstablish()) {
                avChatClient.HangUp();
            }
            avChatClient.enableAVChat(false);
            avChatClient = null;
        }
    }

    public void hangUp() {
        if (avChatClient == null) {
//               SRToast.show("视频模块未初始化");
            return;
        }
        avChatClient.HangUp();
    }

    public AVChatClient getAVChatClient() {
        if (avChatClient == null) {
            SRLog.i("--getAVChatClient()---未初始化AVChatLaunchManager---");
        }
        return avChatClient;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }
}
