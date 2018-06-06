package com.sirui.inquiry.hospital.avchat;

import android.content.Context;
import android.os.SystemClock;
import android.view.ViewGroup;
import android.widget.Toast;

import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatOnlineAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;
import com.sirui.basiclib.utils.SRLog;
import com.sirui.inquiry.hospital.avchat.constants.AVChatExitCode;
import com.sirui.inquiry.hospital.avchat.event.EventType;
import com.sirui.inquiry.hospital.avchat.event.UpdateMsgEvent;
import com.sirui.inquiry.hospital.avchat.receiver.PhoneCallStateObserver;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by xiepc on  2017-08-27 16:50
 */

public class AVChatClient implements AVChatStateObserver {

    private AVChatUI avChatUI;

    private Context context;

    private AVChatStateListener stateListener;
    /**接通视频的时间*/
    private long timeBase;
//    /**视频通话id*/
//    private StringBuffer chatIdBuf = new StringBuffer("");

    public AVChatClient(Context context){
          this.context = context;
    }

    public void enableAVChat(boolean enable){
        registerNetCallObserver(enable);
    }
    /**
     * 切换小窗口时，变换UI实现的状态监听
     * @param stateListener
     */
    public void setAVChatStateListener(AVChatStateListener stateListener){
            if(avChatUI != null){
                avChatUI.setAVChatStateListener(stateListener);
            }
            this.stateListener  = stateListener;
    }
   public void outGoingAVChat(String account){
             timeBase = 0;
             avChatUI = new AVChatUI(context);
             avChatUI.setAVChatStateListener(stateListener);
             avChatUI.outGoingCalling(account, AVChatType.VIDEO);
   }


   public void incomingAVChat(AVChatData avChatData){
        timeBase = 0;
        avChatUI = new AVChatUI(context);
        avChatUI.setAVChatStateListener(stateListener);
        avChatUI.receiveInComingCall(avChatData);
   }
    /**
     * 注册监听
     *
     * @param register
     */
    private void registerNetCallObserver(boolean register) {
        AVChatManager.getInstance().observeAVChatState(this, register);
        AVChatManager.getInstance().observeCalleeAckNotification(callAckObserver, register);
        AVChatManager.getInstance().observeControlNotification(callControlObserver, register);
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, register);
        AVChatManager.getInstance().observeOnlineAckNotification(onlineAckObserver, register);
        AVChatManager.getInstance().observeTimeoutNotification(timeoutObserver, register);
        PhoneCallStateObserver.getInstance().observeAutoHangUpForLocalPhone(autoHangUpForLocalPhoneObserver, register);

    }

    /**
     * 注册/注销网络通话被叫方的响应（接听、拒绝、忙）
     */
    Observer<AVChatCalleeAckEvent> callAckObserver = new Observer<AVChatCalleeAckEvent>() {
        @Override
        public void onEvent(AVChatCalleeAckEvent ackInfo) {
//            if(chatIdBuf.length() == 0){
//                chatIdBuf.append(ackInfo.getChatId());
//            }else{
//                chatIdBuf.append(",");
//                chatIdBuf.append(ackInfo.getChatId());
//            }
//            InquiryOrderInfo orderInfo = InquiryMemberManager.getInstance().getInquiryOrderInfo(ackInfo.getAccount());
//            if(orderInfo != null){
//                orderInfo.appendChatId(String.valueOf(ackInfo.getChatId()));
//            }
            SRLog.i("对方视频响应---ChatId="+ ackInfo.getChatId());

            AVChatSoundPlayer.instance().stop();

            if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {

                AVChatSoundPlayer.instance().play(AVChatSoundPlayer.RingerTypeEnum.PEER_BUSY);

                avChatUI.closeSessions(AVChatExitCode.PEER_BUSY);
            } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                avChatUI.closeRtc();
                avChatUI.closeSessions(AVChatExitCode.REJECT);
            } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                 avChatUI.isCallEstablish.set(true);
//                avChatUI.canSwitchCamera = true;
            }
        }
    };

    Observer<Long> timeoutObserver = new Observer<Long>() {
        @Override
        public void onEvent(Long chatId) {
            SRLog.i("----来电超时，自己未接听----");
            AVChatData info = avChatUI.getAvChatData();
            if (info != null && info.getChatId() == chatId) {

                avChatUI.closeSessions(AVChatExitCode.PEER_NO_RESPONSE);

                // 来电超时，自己未接听
//                if (mIsInComingCall) {
//                    activeMissCallNotifier();
//                }

                AVChatSoundPlayer.instance().stop();
            }

        }
    };

    Observer<Integer> autoHangUpForLocalPhoneObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {

            AVChatSoundPlayer.instance().stop();

            avChatUI.closeSessions(AVChatExitCode.PEER_BUSY);
        }
    };

    /**
     * 注册/注销网络通话控制消息（音视频模式切换通知）
     */
    Observer<AVChatControlEvent> callControlObserver = new Observer<AVChatControlEvent>() {
        @Override
        public void onEvent(AVChatControlEvent netCallControlNotification) {
            handleCallControl(netCallControlNotification);
        }
    };

    /**
     * 注册/注销网络通话对方挂断的通知,网络异常断开
     */
    Observer<AVChatCommonEvent> callHangupObserver = new Observer<AVChatCommonEvent>() {
        @Override
        public void onEvent(AVChatCommonEvent avChatHangUpInfo) {

            AVChatSoundPlayer.instance().stop();
            avChatUI.closeRtc();
            avChatUI.closeSessions(AVChatExitCode.HANGUP);

//            cancelCallingNotifier();
            // 如果是incoming call主叫方挂断，那么通知栏有通知
//            if (mIsInComingCall && !isCallEstablished) {
//                activeMissCallNotifier();
//            }
        }
    };

    /**
     * 注册/注销同时在线的其他端对主叫方的响应
     */
    Observer<AVChatOnlineAckEvent> onlineAckObserver = new Observer<AVChatOnlineAckEvent>() {
        @Override
        public void onEvent(AVChatOnlineAckEvent ackInfo) {

            AVChatSoundPlayer.instance().stop();

            String client = null;
            switch (ackInfo.getClientType()) {
                case ClientType.Web:
                    client = "Web";
                    break;
                case ClientType.Windows:
                    client = "Windows";
                    break;
                case ClientType.Android:
                    client = "Android";
                    break;
                case ClientType.iOS:
                    client = "iOS";
                    break;
                case ClientType.MAC:
                    client = "Mac";
                    break;
                default:
                    break;
            }
            if (client != null) {
                String option = ackInfo.getEvent() == AVChatEventType.CALLEE_ONLINE_CLIENT_ACK_AGREE ? "接听！" : "拒绝！";
                Toast.makeText(context, "通话已在" + client + "端被" + option, Toast.LENGTH_SHORT).show();
            }
            avChatUI.closeSessions(-1);
        }
    };

    /**
     * 处理音视频切换请求
     *
     * @param notification
     */
    private void handleCallControl(AVChatControlEvent notification) {
        switch (notification.getControlCommand()) {
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO:
//                avChatUI.incomingAudioToVideo();
//                break;
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_AGREE:
//                onAudioToVideo();
//                break;
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_REJECT:
//                avChatUI.onCallStateChange(CallStateEnum.AUDIO);
//                Toast.makeText(AVChatActivity.this, R.string.avchat_switch_video_reject, Toast.LENGTH_SHORT).show();
//                break;
//            case AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO:
//                onVideoToAudio();
//                break;
            case AVChatControlCommand.NOTIFY_VIDEO_OFF:
            case AVChatControlCommand.NOTIFY_VIDEO_ON:
                if(stateListener != null){
                     stateListener.onRemoteCamerChange(AVChatControlCommand.NOTIFY_VIDEO_OFF);
                }
                break;
            default:
//                Toast.makeText(context,"对方发来指令值：" + notification.getControlCommand(), Toast.LENGTH_SHORT).show();
                SRLog.i("对方发来指令值：" + notification.getControlCommand());
                break;
        }
    }



    @Override
    public void onJoinedChannel(int code, String audioFile, String videoFile, int elapsed) {

    }

    @Override
    public void onUserJoined(String account) {
        avChatUI.setVideoAccount(account);
        if(stateListener != null){
            stateListener.onUserJoined(account);
        }else{
            SRLog.i("未设置状态监听setAVChatStateListener");
        }
    }

    @Override
    public void onUserLeave(String account, int event) {
        if(stateListener != null) {
            stateListener.onUserLeave(account,event);
        }
        avChatUI.closeRtc();
        avChatUI.closeSessions(AVChatExitCode.HANGUP);
        setAVChatStateListener(null);  //释放外部引用
    }

    @Override
    public void onLeaveChannel() {

    }

    @Override
    public void onProtocolIncompatible(int status) {

    }

    @Override
    public void onDisconnectServer(int code) {

    }

//    @Override
//    public void onDisconnectServer() {
//
//    }

    @Override
    public void onNetworkQuality(String user, int quality, AVChatNetworkStats stats) {

    }

    @Override
    public void onCallEstablished() {
        if (timeBase == 0){
            timeBase = SystemClock.elapsedRealtime();
        }
        if(stateListener != null) {
             stateListener.onCallEstablished();
        }
        EventBus.getDefault().post(new UpdateMsgEvent(EventType.UPDATE_AVCHAT_MSG)); //发送一个更新视频消息的事件
    }

    @Override
    public void onDeviceEvent(int code, String desc) {

    }

    @Override
    public void onTakeSnapshotResult(String account, boolean success, String file) {

    }

    @Override
    public void onConnectionTypeChanged(int netType) {

    }

    @Override
    public void onAVRecordingCompletion(String account, String filePath) {

    }

    @Override
    public void onAudioRecordingCompletion(String filePath) {

    }

    @Override
    public void onLowStorageSpaceWarning(long availableSize) {

    }

    @Override
    public void onFirstVideoFrameAvailable(String account) {

    }

    @Override
    public void onFirstVideoFrameRendered(String user) {

    }

    @Override
    public void onVideoFrameResolutionChanged(String user, int width, int height, int rotate) {

    }

    @Override
    public void onVideoFpsReported(String account, int fps) {

    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
        return false;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
        return false;
    }

    @Override
    public void onAudioDeviceChanged(int device) {

    }

    @Override
    public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {

    }

    @Override
    public void onAudioMixingEvent(int event) {

    }

    @Override
    public void onSessionStats(AVChatSessionStats sessionStats) {

    }

    @Override
    public void onLiveEvent(int event) {

    }

  public void HangUp(){
         avChatUI.onHangUp();
  }

    public void addLocalIntoPreviewLayout(ViewGroup view, boolean isSmallSize) {
            avChatUI.addLocalIntoPreviewLayout(view,isSmallSize);
    }

    public void addRemoteIntoPreviewLayout(ViewGroup view,boolean isSmallSize) {
           avChatUI.addRemoteIntoPreviewLayout(view,isSmallSize);
    }

    public void switchCamera(){
        avChatUI.switchCamera();
    }
    public boolean hasMultipleCameras(){
       return avChatUI.hasMultipleCameras();
    }

    public void toggleMute(){
        avChatUI.toggleMute();
    }

    public void swichPreviewLayout(){
        avChatUI.swichPreviewLayout();
    }
    public boolean isLocalPreviewInSmallSize() {
        return avChatUI.isLocalPreviewInSmallSize();
    }

    public long getTimeBase() {
        return timeBase;
    }

    public void setTimeBase(long timeBase) {
        this.timeBase = timeBase;
    }

    /**
     * 是否属于通话状态
     * @return
     */
    public boolean isCallEstablish(){
        if(avChatUI == null || avChatUI.isCallEstablish == null){
            return false;
        }
        return  avChatUI.isCallEstablish.get();
    }

//    public StringBuffer getChatIdBuf() {
//        return chatIdBuf;
//    }
}
