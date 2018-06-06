package com.sirui.inquiry.hospital.avchat.floatshow;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.ViewTreeObserver;

import com.net.client.event.MainEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.sirui.basiclib.utils.SRLog;
import com.sirui.inquiry.hospital.avchat.AVChatClientManager;
import com.sirui.inquiry.hospital.avchat.AVChatStateListener;
import com.sirui.inquiry.hospital.avchat.event.EventType;

import org.greenrobot.eventbus.EventBus;

public class FloatWindowService extends Service implements FloatWindow.OnClickCallback,AVChatStateListener {

    private FloatWindow floatWindow;
    public FloatWindowService() {

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(AVChatClientManager.getInstance().getAVChatClient() != null){
            floatWindow = new FloatWindow(this);
            AVChatClientManager.getInstance().getAVChatClient().addRemoteIntoPreviewLayout(floatWindow.getContainerView(),true);
            floatWindow.setOnClickCallback(this);

            floatWindow.getContainerView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //布局加载完再注册监听，否则会被AVChatActivity中onDestroy里空注册覆盖
                    if(AVChatClientManager.getInstance().getAVChatClient() != null){
                        AVChatClientManager.getInstance().getAVChatClient().setAVChatStateListener(FloatWindowService.this);
                    }
                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
         if(floatWindow != null){
             floatWindow.removeView();
         }
         if(AVChatClientManager.getInstance().getAVChatClient() != null){
            AVChatClientManager.getInstance().getAVChatClient().setAVChatStateListener(null);
         }
        super.onDestroy();
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, FloatWindowService.class);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, FloatWindowService.class);
        context.stopService(intent);
    }

    @Override
    public void onClick() {
         stopSelf();
         EventBus.getDefault().post(new MainEvent(EventType.SMALL_TO_LARGE_WINDOW));
    }

    @Override
    public void closeSessions(int exitCode) {
          SRLog.i("------FloatWindowService -----closeSessions---------------");
          stopSelf();
    }

    @Override
    public void onCalling(AVChatData data) {

    }

    @Override
    public void onUserJoined(String account) {

    }

    @Override
    public void onUserLeave(String account, int event) {
        SRLog.i("-------------FloatWindowService用户离开-------------");
        AVChatClientManager.getInstance().getAVChatClient().HangUp();
    }

    @Override
    public void onCallEstablished() {

    }

    @Override
    public void onRemoteCamerChange(int stateCode) {
        SRLog.i("-------------FloatWindowService对方摄像头状态改变-------------stateCode = "+stateCode);
    }
}
